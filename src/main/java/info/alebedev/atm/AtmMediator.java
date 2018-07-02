package info.alebedev.atm;

import info.alebedev.atm.hardware.CardReader;
import info.alebedev.atm.hardware.CashDispenser;
import info.alebedev.atm.hardware.CashReceiver;
import info.alebedev.atm.server.Server;
import info.alebedev.atm.state.*;
import info.alebedev.atm.state.factory.AtmStateFactory;
import info.alebedev.atm.state.factory.impl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manages ATM states
 */
class AtmMediator implements StateContext {

    private Map<Class<? extends AtmState>, AtmStateFactory> factoryMap = new HashMap<>();
    private volatile AtmState currentState;


    public AtmMediator(CardReader cardReader, CashReceiver cashReceiver, CashDispenser cashDispenser,
                       Server server) {

        Objects.requireNonNull(cardReader);
        Objects.requireNonNull(cashReceiver);
        Objects.requireNonNull(cashDispenser);
        Objects.requireNonNull(server);

        factoryMap.put(CardAwaitingState.class, new CardAwaitingStateFactory(cardReader));
        factoryMap.put(PinEnterState.class, new PinEnterStateFactory(cardReader, server));
        factoryMap.put(OperationSelectionState.class, new OperationSelectionStateFactory(cardReader, server));
        factoryMap.put(WithdrawState.class, new WithdrawStateFactory(server, cashDispenser));
        factoryMap.put(DepositMakingState.class, new DepositMakingStateFactory(server, cashReceiver));
    }

    /**
     * An entry point of work with ATM
     */
    public void runWorkflow() {
        this.currentState = factoryMap.get(CardAwaitingState.class).createState(null);
        do {
            this.currentState.runWorkflow(this);
        } while (true);
    }

    @Override
    public <T> void setState(Class<? extends AtmState> stateClass, T input) {
        this.currentState = factoryMap.get(stateClass).createState(input);
    }

    @Override
    public void setState(Class<? extends AtmState> stateClass) {
        setState(stateClass, null);
    }
}
