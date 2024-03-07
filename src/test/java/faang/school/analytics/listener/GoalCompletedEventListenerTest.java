package faang.school.analytics.listener;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    @Captor
    private ArgumentCaptor<AnalyticsEvent> argumentCaptor;



    @Test
    void testProcessEventCallsMethod() {
        GoalCompletedEvent goalCompletedEvent = GoalCompletedEvent.builder().build();
        goalCompletedEventListener.processEvent(goalCompletedEvent);
        verify(analyticsEventMapper, times(1)).toAnalyticsEvent(goalCompletedEvent);
        verify(analyticsEventService, times(1)).
                saveEvent(AnalyticsEvent.builder().eventType(EventType.GOAL_COMPLETED).build());
    }
}