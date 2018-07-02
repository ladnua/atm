package info.alebedev.atm.state;

import info.alebedev.atm.hardware.CardReader;

/**
 * Contains logic of ATM card awaiting screen
 */
public class CardAwaitingState implements AtmState {

    private CardReader cardReader;

    public CardAwaitingState(CardReader cardReader) {
        this.cardReader = cardReader;
    }

    @Override
    public void runWorkflow(StateContext stateContext) {
        System.out.println("Please, insert your card");
        String cardNumber = cardReader.waitForCardInsertion();
        stateContext.setState(PinEnterState.class, cardNumber);
    }
}
