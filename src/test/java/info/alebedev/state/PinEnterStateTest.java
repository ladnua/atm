package info.alebedev.state;

import info.alebedev.atm.hardware.CardReader;
import info.alebedev.atm.model.CardNumberAndPin;
import info.alebedev.atm.server.Server;
import info.alebedev.atm.state.CardAwaitingState;
import info.alebedev.atm.state.OperationSelectionState;
import info.alebedev.atm.state.PinEnterState;
import info.alebedev.atm.state.StateContext;
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
public class PinEnterStateTest {

    @Mock
    private CardReader cardReader;
    @Mock
    private Server server;
    @Mock
    private StateContext stateContext;


    @Test
    public void runWorkflowValidPinTest() throws IOException {

        final String mockPin = "1111";
        try (ByteArrayInputStream userInputStream = new ByteArrayInputStream(mockPin.getBytes())) {

            System.setIn(userInputStream);

            final String mockCardNumber = "1234567890123456";
            PinEnterState state = new PinEnterState(cardReader, server, mockCardNumber);

            Mockito.when(server.isPinValid(new CardNumberAndPin(mockCardNumber, mockPin))).thenReturn(true);

            state.runWorkflow(stateContext);

            Mockito.verify(stateContext).setState(OperationSelectionState.class, new CardNumberAndPin(mockCardNumber, mockPin));
        }
    }

    @Test
    public void runWorkflowInvalidPinTest() throws IOException {

        final String mockPin = "1111";

        try (ByteArrayInputStream userInputStream = new ByteArrayInputStream(mockPin.getBytes())) {
            System.setIn(userInputStream);

            final String mockCardNumber = "1234567890123456";
            PinEnterState state = new PinEnterState(cardReader, server, mockCardNumber);

            Mockito.when(server.isPinValid(new CardNumberAndPin(mockCardNumber, mockPin))).thenReturn(false);

            state.runWorkflow(stateContext);

            Mockito.verify(cardReader).ejectCard();
            Mockito.verify(stateContext).setState(CardAwaitingState.class);
        }
    }
}
