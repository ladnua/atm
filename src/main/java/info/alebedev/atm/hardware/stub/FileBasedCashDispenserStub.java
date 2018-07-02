package info.alebedev.atm.hardware.stub;

import info.alebedev.atm.hardware.CashDispenser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * {@link CashDispenser} implementation based on file output
 */
public class FileBasedCashDispenserStub implements CashDispenser {

    private File file;

    public FileBasedCashDispenserStub(File file) {
        this.file = file;
    }

    /**
     * Re-crates the specified file and puts the given amount into it
     *
     * @param amount amount of cash to be given
     */
    @Override
    public void giveCash(int amount) {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(String.valueOf(amount).getBytes());
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }
}
