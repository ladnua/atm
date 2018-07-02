package info.alebedev.atm.state;

/**
 * Defines a protocol of work with ATM states
 */
public interface AtmState {

    /**
     * Runs workflow of an ATM state (e.g. requires some input from a user
     * according to the current screen)
     * @param stateContext ATM state context
     */
    void runWorkflow(StateContext stateContext);
}
