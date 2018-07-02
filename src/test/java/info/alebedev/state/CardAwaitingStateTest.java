package info.alebedev.state;

import info.alebedev.atm.hardware.CardReader;
import info.alebedev.atm.state.CardAwaitingState;
import info.alebedev.atm.state.PinEnterState;
import info.alebedev.atm.state.StateContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by aleksey on 02.07.18.
 */
@RunWith(MockitoJUnitRunner.class)
public class CardAwaitingStateTest {

    @Mock
    private CardReader cardReader;
    @Mock
    private StateContext stateContext;

    @Test
    public void runWorkflowTest() {
        CardAwaitingState state = new CardAwaitingState(cardReader);

        final String mockCardNumber = "1234567890123456";
        Mockito.when(cardReader.waitForCardInsertion()).thenReturn(mockCardNumber);

        state.runWorkflow(stateContext);

        Mockito.verify(stateContext).setState(PinEnterState.class, mockCardNumber);
    }
}
