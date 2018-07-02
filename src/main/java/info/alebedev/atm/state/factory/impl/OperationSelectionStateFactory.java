package info.alebedev.atm.state.factory.impl;

import info.alebedev.atm.hardware.CardReader;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;
import info.alebedev.atm.state.AtmState;
import info.alebedev.atm.state.OperationSelectionState;
import info.alebedev.atm.state.factory.AtmStateFactory;

/**
 * Creates {@link OperationSelectionState}
 */
public class OperationSelectionStateFactory implements AtmStateFactory<CardNumberAndPin> {

    private CardReader cardReader;
    private Server server;

    public OperationSelectionStateFactory(CardReader cardReader, Server server) {
        this.cardReader = cardReader;
        this.server = server;
    }

    @Override
    public AtmState createState(CardNumberAndPin input) {
        return new OperationSelectionState(server, cardReader, input);
    }
}
