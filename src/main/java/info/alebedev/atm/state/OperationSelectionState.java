package info.alebedev.atm.state;

import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.hardware.CardReader;
import info.alebedev.atm.server.Server;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Contains logic of ATM operation selection screen
 */
public class OperationSelectionState implements AtmState {


    private static final int CHECK_BALANCE_CODE = 1;
    private static final int WITHDRAW_CODE = 2;
    private static final int MAKE_DEPOSIT_CODE = 3;
    private static final int END_WORKING_CODE = 4;

    private Server server;
    private CardReader cardReader;
    private CardNumberAndPin cardNumberAndPin;

    public OperationSelectionState(Server server, CardReader cardReader, CardNumberAndPin cardNumberAndPin) {
        this.server = server;
        this.cardReader = cardReader;
        this.cardNumberAndPin = cardNumberAndPin;
    }

    @Override
    public void runWorkflow(StateContext stateContext) {

        System.out.println("Select operation");
        System.out.println("1. Check balance");
        System.out.println("2. Withdraw cash");
        System.out.println("3. Make deposit");
        System.out.println("4. End working with ATM");

        Scanner inputScanner = new Scanner(System.in);

        try {
            int option = inputScanner.nextInt();

            switch (option) {
                case CHECK_BALANCE_CODE:
                    System.out.println("Your balance is " + server.getBalance(cardNumberAndPin));
                    break;

                case MAKE_DEPOSIT_CODE:
                    stateContext.setState(DepositMakingState.class, cardNumberAndPin);
                    break;

                case WITHDRAW_CODE:
                    stateContext.setState(WithdrawState.class, cardNumberAndPin);
                    break;

                case END_WORKING_CODE:
                    System.out.println("Take your card");
                    cardReader.ejectCard();
                    stateContext.setState(CardAwaitingState.class);
                    break;
            }
        } catch (InputMismatchException e) {

        }
    }
}

