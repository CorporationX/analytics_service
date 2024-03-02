package faang.school.analytics.listener;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.service.GoalCompletedEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {
    @Mock
    private GoalCompletedEventService goalCompletedEventService;
    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;

    @Test
    void testProcessEventCallsMethod() {
        GoalCompletedEvent goalCompletedEvent = new GoalCompletedEvent();
        goalCompletedEventListener.processEvent(goalCompletedEvent);
        verify(goalCompletedEventService, times(1)).save(goalCompletedEvent);
    }
}