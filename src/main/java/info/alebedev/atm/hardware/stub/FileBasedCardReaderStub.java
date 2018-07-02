package info.alebedev.atm.hardware.stub;

import info.alebedev.atm.hardware.CardReader;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * {@link CardReader} based on a file polling
 * Once the file contains 16 digit number in the first line
 * it is considered to be a number of the inserted card and
 * registered card insertion listener is inserted
 *
 * File polling is done in a separate thread. Listener invocation is
 * done in the same thread as waitForCardInsertion is invoked in
 */
public class FileBasedCardReaderStub implements CardReader {

    private static final String CARD_NUMBER_REGEXP = "^[0-9]{16}$";
    private static final int FILE_POLLING_PERIOD = 2000;

    private File file;
    private String insertedCardNumber;

    public FileBasedCardReaderStub(File file) {
        this.file = file;
    }

    @Override
    public String waitForCardInsertion() {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> cardNumberFuture = executor.submit(this::getCardNumberFromFile);

        try {
            insertedCardNumber = cardNumberFuture.get();
            file.delete();
            file.createNewFile();
            return insertedCardNumber;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            executor.shutdown();
        }
    }

    @Override
    public void ejectCard() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(("EJECTED CARD:" + insertedCardNumber).getBytes());
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private String getCardNumberFromFile() throws InterruptedException, IOException {

        while (!Thread.interrupted()) {

            if (file.exists()) {
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    Scanner scanner = new Scanner(inputStream);
                    String fileLine = scanner.next();
                    if (fileLine.matches(CARD_NUMBER_REGEXP)) {
                        return fileLine;
                    }
                } catch (NoSuchElementException e) {

                }

                Thread.sleep(FILE_POLLING_PERIOD);
            }
        }

        return null;
    }
}
