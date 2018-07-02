package info.alebedev.atm.state;

import info.alebedev.atm.hardware.CashDispenser;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;

import java.util.Scanner;

/**
 * Contains logic of ATM money withdrawal screen
 */
public class WithdrawState implements AtmState {

    private Server server;
    private CashDispenser cashDispenser;
    private CardNumberAndPin cardNumberAndPin;

    public WithdrawState(Server server, CashDispenser cashDispenser, CardNumberAndPin cardNumberAndPin) {
        this.server = server;
        this.cashDispenser = cashDispenser;
        this.cardNumberAndPin = cardNumberAndPin;
    }

    @Override
    public void runWorkflow(StateContext stateContext) {
        System.out.println("Input amount of cash to withdraw");
        Scanner inputScanner = new Scanner(System.in);
        int amount = inputScanner.nextInt();

        if (amount <= 0) {
            System.out.println("Amount should be positive");
            stateContext.setState(OperationSelectionState.class, cardNumberAndPin);
            return;
        }

        int balance = server.getBalance(cardNumberAndPin);
        if (balance < amount) {
            System.out.println("Not enough money");
        } else {
            server.withdrawCash(cardNumberAndPin, amount);
            cashDispenser.giveCash(amount);
            System.out.println("Take amount of cash ordered");
        }

        stateContext.setState(OperationSelectionState.class, cardNumberAndPin);
    }
}
