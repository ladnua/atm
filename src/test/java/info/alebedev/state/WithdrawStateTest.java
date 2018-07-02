package info.alebedev.state;

import info.alebedev.atm.hardware.CashDispenser;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;
import info.alebedev.atm.state.OperationSelectionState;
import info.alebedev.atm.state.StateContext;
import info.alebedev.atm.state.WithdrawState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by aleksey on 02.07.18.
 */
@RunWith(MockitoJUnitRunner.class)
public class WithdrawStateTest {

    @Mock
    private Server server;
    @Mock
    private CashDispenser cashDispenser;
    @Mock
    private StateContext stateContext;


    @Test
    public void runWorkflowSuccessTest() throws IOException {

        try (ByteArrayInputStream userInputStream = new ByteArrayInputStream("100".getBytes())) {
            System.setIn(userInputStream);

            CardNumberAndPin mockCardNumberAndPin = new CardNumberAndPin("1234567890123456", "1111");
            WithdrawState state = new WithdrawState(server, cashDispenser, mockCardNumberAndPin);


            Mockito.when(server.getBalance(mockCardNumberAndPin)).thenReturn(200);
            state.runWorkflow(stateContext);

            Mockito.verify(server).withdrawCash(mockCardNumberAndPin, 100);
            Mockito.verify(cashDispenser).giveCash(100);
            Mockito.verify(stateContext).setState(OperationSelectionState.class, mockCardNumberAndPin);
        }
    }


    @Test
    public void runWorkflowNotEnoughMoneyTest() throws IOException {

        try (ByteArrayInputStream userInputStream = new ByteArrayInputStream("300".getBytes());
             ByteArrayOutputStream userOutputStream = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(userOutputStream)) {

            System.setIn(userInputStream);
            System.setOut(printStream);


            CardNumberAndPin mockCardNumberAndPin = new CardNumberAndPin("1234567890123456", "1111");
            WithdrawState state = new WithdrawState(server, cashDispenser, mockCardNumberAndPin);


            Mockito.when(server.getBalance(mockCardNumberAndPin)).thenReturn(200);
            state.runWorkflow(stateContext);

            Mockito.verify(server, Mockito.times(0)).withdrawCash(mockCardNumberAndPin, 100);
            Mockito.verify(cashDispenser, Mockito.times(0)).giveCash(100);
            Mockito.verify(stateContext).setState(OperationSelectionState.class, mockCardNumberAndPin);

            assertEquals("Not enough money", userOutputStream.toString().split(System.lineSeparator())[1]);
        }
    }

}
