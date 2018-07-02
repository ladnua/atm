package info.alebedev.atm.state.factory.impl;

import info.alebedev.atm.hardware.CardReader;
import info.alebedev.atm.state.AtmState;
import info.alebedev.atm.state.CardAwaitingState;
import info.alebedev.atm.state.factory.AtmStateFactory;

/**
 * Creates {@link CardAwaitingState}
 */
public class CardAwaitingStateFactory implements AtmStateFactory<Void> {

    private CardReader cardReader;


    public CardAwaitingStateFactory(CardReader cardReader) {
        this.cardReader = cardReader;
    }

    @Override
    public AtmState createState(Void input) {
        return new CardAwaitingState(cardReader);
    }
}
