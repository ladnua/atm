package info.alebedev.atm.state;

import info.alebedev.atm.hardware.CardReader;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;

import java.util.Scanner;

/**
 * Contains logic of ATM PIN code enter screen
 */
public class PinEnterState implements AtmState {

    private CardReader cardReader;
    private Server server;
    private String cardNumber;

    public PinEnterState(CardReader cardReader, Server server, String cardNumber) {
        this.cardReader = cardReader;
        this.server = server;
        this.cardNumber = cardNumber;
    }

    @Override
    public void runWorkflow(StateContext stateContext) {
        System.out.println("Please, enter your PIN code");
        Scanner inputScanner = new Scanner(System.in);
        String pin = inputScanner.next();

        CardNumberAndPin cardNumberAndPin = new CardNumberAndPin(cardNumber, pin);

        if (server.isPinValid(cardNumberAndPin)) {
            stateContext.setState(OperationSelectionState.class, cardNumberAndPin);
        } else {
            System.out.println("PIN is not valid. Take your card.");
            cardReader.ejectCard();
            stateContext.setState(CardAwaitingState.class);
        }
    }
}
