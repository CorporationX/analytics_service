package faang.school.analytics.service;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    @Captor
    private ArgumentCaptor<AnalyticsEvent> argumentCaptor;
    @InjectMocks
    private GoalCompletedEventService goalCompletedEventService;
    @Test
    void testSaveSavesAnalytics() {
        GoalCompletedEvent goalCompletedEvent = GoalCompletedEvent.builder().build();
        goalCompletedEventService.save(goalCompletedEvent);
        Mockito.verify(analyticsEventMapper).toAnalyticsEvent(goalCompletedEvent);
        Mockito.verify(analyticsEventRepository,times(1)).save(argumentCaptor.capture());
        AnalyticsEvent analyticsEvent1 = argumentCaptor.getValue();
        assertEquals(goalCompletedEvent.getUserId(),analyticsEvent1.getReceiverId());
    }
}