package info.alebedev.atm.state;

/**
 * Defines the protocol of work with ATM state context
 */
public interface StateContext {

    /**
     * Sets next ATM sate after the current one ends working
     *
     * @param stateClass next state class
     * @param input next state input data
     * @param <T> next state input data type
     */
    <T> void setState(Class<? extends AtmState> stateClass, T input);

    /**
     * Sets next ATM sate after the current one ends working
     * without passing state input date
     *
     * @param stateClass next state class
     */
    void setState(Class<? extends AtmState> stateClass);
}
