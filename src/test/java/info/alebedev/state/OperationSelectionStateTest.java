package info.alebedev.state;

import info.alebedev.atm.hardware.CardReader;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;
import info.alebedev.atm.state.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by aleksey on 02.07.18.
 */
@RunWith(MockitoJUnitRunner.class)
public class OperationSelectionStateTest {

    @Mock
    private Server server;
    @Mock
    private CardReader cardReader;
    @Mock
    private StateContext stateContext;


    @Test
    public void runWorkflowCheckBalanceTest() throws IOException {

        try (ByteArrayInputStream userInputStream = new ByteArrayInputStream("1".getBytes())) {

            System.setIn(userInputStream);

            CardNumberAndPin mockCardNumberAndPin = new CardNumberAndPin("1234567890123456", "1111");
            OperationSelectionState state = new OperationSelectionState(server, cardReader, mockCardNumberAndPin);

            state.runWorkflow(stateContext);

            Mockito.verify(server).getBalance(mockCardNumberAndPin);
        }
    }

    @Test
    public void runWorkflowWithdrawCashTest() throws IOException {

        try (ByteArrayInputStream userInputStream = new ByteArrayInputStream("2".getBytes())) {


            System.setIn(userInputStream);

            CardNumberAndPin mockCardNumberAndPin = new CardNumberAndPin("1234567890123456", "1111");
            OperationSelectionState state = new OperationSelectionState(server, cardReader, mockCardNumberAndPin);

            state.runWorkflow(stateContext);
            Mockito.verify(stateContext).setState(WithdrawState.class, mockCardNumberAndPin);
        }
    }

    @Test
    public void runWorkflowMakeDepositTest() {

        CardNumberAndPin mockCardNumberAndPin = new CardNumberAndPin("1234567890123456", "1111");
        OperationSelectionState state = new OperationSelectionState(server, cardReader, mockCardNumberAndPin);

        ByteArrayInputStream userInputStream = new ByteArrayInputStream("3".getBytes());
        System.setIn(userInputStream);

        state.runWorkflow(stateContext);
        Mockito.verify(stateContext).setState(DepositMakingState.class, mockCardNumberAndPin);
    }


    @Test
    public void runWorkflowEndWorkTest() {

        CardNumberAndPin mockCardNumberAndPin = new CardNumberAndPin("1234567890123456", "1111");
        OperationSelectionState state = new OperationSelectionState(server, cardReader, mockCardNumberAndPin);

        ByteArrayInputStream userInputStream = new ByteArrayInputStream("4".getBytes());
        System.setIn(userInputStream);

        state.runWorkflow(stateContext);

        Mockito.verify(cardReader).ejectCard();
        Mockito.verify(stateContext).setState(CardAwaitingState.class);
    }
}
