package info.alebedev.atm.hardware.stub;

import info.alebedev.atm.hardware.CashReceiver;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * {@link CashReceiver} based on a file polling
 * Once the file contains a number in the first line
 * it is considered to be an amount of inserted cash
 * registered card insertion listener is inserted
 *
 * File polling is done in a separate thread. Listener invocation is
 * done in that thread
 */
public class FileBasedCashReceiverStub implements CashReceiver {

    private static final String AMOUNT_REGEXP = "^[0-9]+$";
    private static final int FILE_POLLING_PERIOD = 2000;

    private File file;
    private Consumer<Integer> cashAcceptedListener;
    private Thread filePollingThread;
    private int amountInside;

    public FileBasedCashReceiverStub(File file) {
        this.file = file;
    }

    @Override
    public void enable() {
        filePollingThread = new Thread(this::pollFile);
        filePollingThread.start();
    }

    @Override
    public void disable() {
        filePollingThread.interrupt();
        filePollingThread = null;
    }

    @Override
    public void ejectMoney() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(("EJECTED AMOUNT:" + amountInside).getBytes());
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            amountInside = 0;
        }
    }

    @Override
    public void acceptMoney() {
        amountInside = 0;
    }

    @Override
    public void registerCashAcceptedListener(Consumer<Integer> listener) {
        this.cashAcceptedListener = listener;
    }

    private void pollFile() {

        while (!Thread.interrupted()) {
            if (file.exists()) {

                boolean cashAccepted = false;
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    Scanner scanner = new Scanner(inputStream);
                    String fileLine = scanner.next();
                    if (fileLine.matches(AMOUNT_REGEXP) && cashAcceptedListener != null) {
                        int amountAccepted = Integer.valueOf(fileLine);
                        amountInside += amountAccepted;
                        this.cashAcceptedListener.accept(amountAccepted);
                        cashAccepted = true;
                    }
                } catch (NoSuchElementException e) {
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (cashAccepted) {
                    file.delete();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(FILE_POLLING_PERIOD);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
