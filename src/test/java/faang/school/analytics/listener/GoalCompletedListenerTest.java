package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedListenerTest {

    @Mock
    private ObjectMapper mapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private GoalCompletedListener listener;

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
    void onMessage() throws IOException {

        Message message = new DefaultMessage(new byte[]{}, new byte[]{});
        byte[] pattern = new byte[]{};

        when(mapper.readValue(message.getBody(), GoalCompletedEvent.class)).thenReturn(event);
        when(analyticsEventMapper.toAnalyticsEvent(event)).thenReturn(analyticsEvent);
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);

        listener.onMessage(message, pattern);
        assertEquals(analyticsEvent.getEventType(), EventType.GOAL_COMPLETED);

        InOrder inOrder = inOrder(mapper, analyticsEventRepository, analyticsEventMapper);
        inOrder.verify(mapper).readValue(message.getBody(), GoalCompletedEvent.class);
        inOrder.verify(analyticsEventMapper).toAnalyticsEvent(event);
        inOrder.verify(analyticsEventRepository).save(analyticsEvent);
    }
}