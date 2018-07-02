package info.alebedev.atm.server.stub;

import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.InvalidPinException;
import info.alebedev.atm.server.Server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ATM bank server stub implementation based on CSV file data base
 * The CSV file should have the following format
 * CARD NUMBER;PIN CODE;BALANCE
 * No header is needed
 */
public class FileBasedServerStub implements Server {

    private static final int FILE_FIELD_NUMBER = 3;
    private static final String FIELD_SEPARATOR = ";";

    public static final int CARD_NUMBER_INDEX = 0;
    public static final int PIN_INDEX = 1;
    public static final int BALANCE_INDEX = 2;

    private Map<String, DbRecord> dbRecords = new HashMap<>();

    public FileBasedServerStub(File dbFile) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(dbFile))) {
            String line = reader.readLine();

            while(line != null) {
                String[] fields = line.split(FIELD_SEPARATOR);
                if (fields.length != FILE_FIELD_NUMBER) {
                    throw new IllegalArgumentException("File has an incorrect format");
                }

                String cardNumber = fields[CARD_NUMBER_INDEX];
                String pin = fields[PIN_INDEX];
                int balance = Integer.valueOf(fields[BALANCE_INDEX]);

                dbRecords.put(cardNumber, new DbRecord(cardNumber, pin, balance));

                line = reader.readLine();
            }
        }
    }

    /**
     * Verifies PIN code against the supplied CSV file
     *
     * @param cardNumberAndPin card number an PIN code
     * @return true if PIN is valid, false otherwise
     */
    @Override
    public boolean isPinValid(CardNumberAndPin cardNumberAndPin) {
        Objects.requireNonNull(cardNumberAndPin);

        if (!dbRecords.containsKey(cardNumberAndPin.getCardNumber())) {
            return false;
        }

        return dbRecords.get(cardNumberAndPin.getCardNumber()).getPin().equals(cardNumberAndPin.getPin());
    }


    /**
     * Returns current card balance
     *
     * @param cardNumberAndPin card number an PIN code
     * @return current card balance
     */
    @Override
    public int getBalance(CardNumberAndPin cardNumberAndPin) {
        DbRecord dbRecord = authenticateAndGetRecord(cardNumberAndPin);
        return dbRecord.getBalance();
    }


    /**
     * Decreases card balance on a server
     *
     * @param cardNumberAndPin card number an PIN code
     * @param amount amount by which balance should be decreased
     */
    @Override
    public void withdrawCash(CardNumberAndPin cardNumberAndPin, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount should be at least 1");
        }
        DbRecord dbRecord = authenticateAndGetRecord(cardNumberAndPin);
        dbRecord.setBalance(dbRecord.getBalance() - amount);
    }

    /**
     * Increases card balance on a server
     *
     * @param cardNumberAndPin card number an PIN code
     * @param amount amount by which balance should be increased
     */
    @Override
    public void depositMoney(CardNumberAndPin cardNumberAndPin, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount should be at least 1");
        }
        DbRecord dbRecord = authenticateAndGetRecord(cardNumberAndPin);
        dbRecord.setBalance(dbRecord.getBalance() + amount);
    }

    private DbRecord authenticateAndGetRecord(CardNumberAndPin cardNumberAndPin) {
        Objects.requireNonNull(cardNumberAndPin);

        if (!dbRecords.containsKey(cardNumberAndPin.getCardNumber())) {
            throw new IllegalArgumentException("Card does not exist");
        }

        DbRecord dbRecord = dbRecords.get(cardNumberAndPin.getCardNumber());

        if (!dbRecord.getPin().equals(cardNumberAndPin.getPin())) {
            throw new InvalidPinException();
        }
        return dbRecord;
    }
}
