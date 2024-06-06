package faang.school.analytics.service;

import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedAnalyticsServiceTest {

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private GoalCompletedAnalyticsService goalCompletedAnalyticsService;

    private GoalCompletedEvent event;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        event = GoalCompletedEvent.builder()
                .goalId(1L)
                .userId(1L)
                .completedAt(LocalDateTime.now())
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .id(4L)
                .actorId(1L)
                .receiverId(1L)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void save() {
        when(analyticsEventMapper.toAnalyticsEvent(event)).thenReturn(analyticsEvent);

        goalCompletedAnalyticsService.save(event);
        assertEquals(EventType.GOAL_COMPLETED, analyticsEvent.getEventType());

        InOrder inOrder = inOrder(analyticsEventRepository, analyticsEventMapper);
        inOrder.verify(analyticsEventMapper).toAnalyticsEvent(event);
        inOrder.verify(analyticsEventRepository).save(analyticsEvent);
    }
}