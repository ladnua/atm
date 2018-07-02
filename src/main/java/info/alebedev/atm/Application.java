package info.alebedev.atm;

import info.alebedev.atm.hardware.stub.FileBasedCardReaderStub;
import info.alebedev.atm.hardware.stub.FileBasedCashDispenserStub;
import info.alebedev.atm.hardware.stub.FileBasedCashReceiverStub;
import info.alebedev.atm.server.stub.FileBasedServerStub;

import java.io.File;
import java.io.IOException;

/**
 * Main application class
 * Program entry point
 */
public class Application {

    private static AtmMediator atmMediator;

    public static void main(String[] args) throws IOException {

        atmMediator = new AtmMediator(
                new FileBasedCardReaderStub(new File("card reader")),
                new FileBasedCashReceiverStub(new File("cash receiver")),
                new FileBasedCashDispenserStub(new File("cash dispenser")),
                new FileBasedServerStub(new File("server db")));

        atmMediator.runWorkflow();
    }
}
