package info.alebedev.atm.state.factory;

import info.alebedev.atm.state.AtmState;

/**
 * Defines methods set of factory creating ATM states
 */
public interface AtmStateFactory<T> {

    AtmState createState(T input);
}
