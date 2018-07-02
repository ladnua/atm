package info.alebedev.state;

import info.alebedev.atm.hardware.CashReceiver;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;
import info.alebedev.atm.state.DepositMakingState;
import info.alebedev.atm.state.StateContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class DepositMakingStateTest {

    @Mock
    private Server server;
    @Mock
    private CardNumberAndPin cardNumberAndPin;
    @Mock
    private CashReceiver cashReceiver;
    @Mock
    private StateContext stateContext;


    @Test
    public void runWorkflowDepositTest() throws IOException {

        try (ByteArrayInputStream userInputStream = new ByteArrayInputStream("1".getBytes())) {
            System.setIn(userInputStream);

            CardNumberAndPin mockCardNumberAndPin = new CardNumberAndPin("1234567890123456", "1111");

            Mockito.doAnswer(a -> {
                    ((Consumer)a.getArguments()[0]).accept(200);
                    return null;
                }).when(cashReceiver).registerCashAcceptedListener(Mockito.any(Consumer.class));

            DepositMakingState state = new DepositMakingState(server, mockCardNumberAndPin, cashReceiver);

            state.runWorkflow(stateContext);

            Mockito.verify(cashReceiver).acceptMoney();
            Mockito.verify(server).depositMoney(mockCardNumberAndPin, 200);
            Mockito.verify(cashReceiver, Mockito.times(0)).ejectMoney();
        }
    }



    @Test
    public void runWorkflowCancelTest() throws IOException {

        try (ByteArrayInputStream userInputStream = new ByteArrayInputStream("2".getBytes())) {
            System.setIn(userInputStream);

            CardNumberAndPin mockCardNumberAndPin = new CardNumberAndPin("1234567890123456", "1111");

            Mockito.doAnswer(a -> {
                ((Consumer)a.getArguments()[0]).accept(200);
                return null;
            }).when(cashReceiver).registerCashAcceptedListener(Mockito.any(Consumer.class));

            DepositMakingState state = new DepositMakingState(server, mockCardNumberAndPin, cashReceiver);

            state.runWorkflow(stateContext);

            Mockito.verify(cashReceiver, Mockito.times(0)).acceptMoney();
            Mockito.verify(server, Mockito.times(0)).depositMoney(mockCardNumberAndPin, 200);
            Mockito.verify(cashReceiver).ejectMoney();
        }
    }
}
