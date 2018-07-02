package info.alebedev.atm.state.factory.impl;

import info.alebedev.atm.hardware.CashDispenser;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;
import info.alebedev.atm.state.AtmState;
import info.alebedev.atm.state.WithdrawState;
import info.alebedev.atm.state.factory.AtmStateFactory;

/**
 * Creates {@link WithdrawState}
 */
public class WithdrawStateFactory implements AtmStateFactory<CardNumberAndPin> {

    private Server server;
    private CashDispenser cashDispenser;

    public WithdrawStateFactory(Server server, CashDispenser cashDispenser) {
        this.server = server;
        this.cashDispenser = cashDispenser;
    }

    @Override
    public AtmState createState(CardNumberAndPin input) {
        return new WithdrawState(server, cashDispenser, input);
    }
}
