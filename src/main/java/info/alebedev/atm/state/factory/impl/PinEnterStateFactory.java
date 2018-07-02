package info.alebedev.atm.state.factory.impl;

import info.alebedev.atm.hardware.CardReader;
import info.alebedev.atm.server.Server;
import info.alebedev.atm.state.AtmState;
import info.alebedev.atm.state.PinEnterState;
import info.alebedev.atm.state.factory.AtmStateFactory;

/**
 * Creates {@link PinEnterState}
 */
public class PinEnterStateFactory implements AtmStateFactory<String> {

    private CardReader cardReader;
    private Server server;

    public PinEnterStateFactory(CardReader cardReader, Server server) {
        this.cardReader = cardReader;
        this.server = server;
    }

    @Override
    public AtmState createState(String cardNumber) {
        return new PinEnterState(cardReader, server, cardNumber);
    }
}
