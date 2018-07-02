package info.alebedev.atm.state.factory.impl;

import info.alebedev.atm.hardware.CashReceiver;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;
import info.alebedev.atm.state.AtmState;
import info.alebedev.atm.state.DepositMakingState;
import info.alebedev.atm.state.factory.AtmStateFactory;

/**
 * Creates {@link DepositMakingState}
 */
public class DepositMakingStateFactory implements AtmStateFactory<CardNumberAndPin> {

    private Server server;
    private CashReceiver cashReceiver;

    public DepositMakingStateFactory(Server server, CashReceiver cashReceiver) {
        this.server = server;
        this.cashReceiver = cashReceiver;
    }

    @Override
    public AtmState createState(CardNumberAndPin cardNumberAndPin) {
        return new DepositMakingState(server, cardNumberAndPin, cashReceiver);
    }
}
