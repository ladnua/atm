package info.alebedev.atm.hardware;

/**
 * Defines protocol of work with cash dispenser
 */
public interface CashDispenser {

    /**
     * Gives cash to a client
     * @param amount amount of cash to be given
     */
    void giveCash(int amount);
}
