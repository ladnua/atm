package info.alebedev.atm.server;

import info.alebedev.atm.model.CardNumberAndPin;

/**
 * Defines a protocol of work with ATM bank server
 */
public interface Server {

    /**
     * Verifies PIN code
     *
     * @param cardNumberAndPin card number an PIN code
     * @return true if PIN is valid, false otherwise
     */
    boolean isPinValid(CardNumberAndPin cardNumberAndPin);


    /**
     * Returns current card balance
     *
     * @param cardNumberAndPin card number an PIN code
     * @return current card balance
     */
    int getBalance(CardNumberAndPin cardNumberAndPin);

    /**
     * Decreases card balance on a server
     *
     * @param cardNumberAndPin card number an PIN code
     * @param amount amount by which balance should be decreased
     */
    void withdrawCash(CardNumberAndPin cardNumberAndPin, int amount);


    /**
     * Increases card balance on a server
     *
     * @param cardNumberAndPin card number an PIN code
     * @param amount amount by which balance should be increased
     */
    void depositMoney(CardNumberAndPin cardNumberAndPin, int amount);
}
