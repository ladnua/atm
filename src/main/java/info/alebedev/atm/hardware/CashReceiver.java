package info.alebedev.atm.hardware;

import java.util.function.Consumer;

/**
 * Defines protocol of work with cash receiver
 */
public interface CashReceiver {

    /**
     * Enables cash insertion
     */
    void enable();


    /**
     * Disables cash insertion
     */
    void disable();

    /**
     * Ejects inserted cash
     */
    void ejectMoney();

    /**
     * Accepts inserted cash
     */
    void acceptMoney();

    /**
     * Register a listener which will be invoked on cash insertion
     * @param listener call-back
     */
    void registerCashAcceptedListener(Consumer<Integer> listener);
}
