package info.alebedev.atm.hardware;

/**
 * Contains protocol of work with card reader
 */
public interface CardReader {

    /**
     * Waits until card is inserted and returns inserted card number
     * @return inserted card number
     */
    String waitForCardInsertion();

    /**
     * Ejects inserted card
     */
    void ejectCard();
}
