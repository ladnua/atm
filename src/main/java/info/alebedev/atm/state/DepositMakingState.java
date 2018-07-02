package info.alebedev.atm.state;

import info.alebedev.atm.hardware.CashReceiver;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Contains logic of ATM deposit making screen
 */
public class DepositMakingState implements AtmState {

    public static final int DEPOSIT_MONEY_CODE = 1;
    public static final int CANCEL_CODE = 2;

    private Server server;
    private CardNumberAndPin cardNumberAndPin;
    private CashReceiver cashReceiver;
    private int currentAmount;

    public DepositMakingState(Server server, CardNumberAndPin cardNumberAndPin, CashReceiver cashReceiver) {
        this.server = server;
        this.cardNumberAndPin = cardNumberAndPin;
        this.cashReceiver = cashReceiver;
    }

    @Override
    public void runWorkflow(StateContext stateContext) {
        printMenu();
        cashReceiver.registerCashAcceptedListener(this::onCashAccepted);
        cashReceiver.enable();
        Scanner inputScanner = new Scanner(System.in);

        try {

            int option = inputScanner.nextInt();
            cashReceiver.disable();

            switch (option) {
                case DEPOSIT_MONEY_CODE:
                    if (currentAmount > 0) {
                        cashReceiver.acceptMoney();
                        server.depositMoney(cardNumberAndPin, currentAmount);
                    }
                    break;

                case CANCEL_CODE:
                    cashReceiver.ejectMoney();
                    break;
            }

            stateContext.setState(OperationSelectionState.class, cardNumberAndPin);

        } catch (InputMismatchException e) {
        }
    }

    private void printMenu() {
        System.out.println("Insert cash into cash receiver");
        System.out.println("Current amount " + currentAmount);
        System.out.println("1. Deposit money");
        System.out.println("2. Cancel");
    }

    private void onCashAccepted(Integer amountInserted) {
        currentAmount += amountInserted;
        printMenu();
    }
}
