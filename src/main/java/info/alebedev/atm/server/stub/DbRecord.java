package info.alebedev.atm.server.stub;

/**
 * Created by aleksey on 30.06.18.
 */
class DbRecord {

    private String cardNumber;
    private String pin;
    private int balance;

    public DbRecord(String cardNumber, String pin, int balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }
}
