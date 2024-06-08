package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsService;
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

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedListenerTest {

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private GoalCompletedListener goalCompletedListener;

    private GoalCompletedEvent event;
    private AnalyticsEventDto analyticsEvent;

    @BeforeEach
    void setUp() {
        event = GoalCompletedEvent.builder()
                .goalId(1L)
                .userId(1L)
                .completedAt(LocalDateTime.now())
                .build();

        analyticsEvent = AnalyticsEventDto.builder()
                .id(4L)
                .actorId(1L)
                .receiverId(1L)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void onMessage() throws IOException {
        byte[] body = new byte[]{2};
        Message message = new DefaultMessage(new byte[]{1}, body);
        byte[] pattern = new byte[]{3};

        when(objectMapper.readValue(body, GoalCompletedEvent.class)).thenReturn(event);
        when(analyticsEventMapper.toAnalyticsEvent(event)).thenReturn(analyticsEvent);

        goalCompletedListener.onMessage(message, pattern);

        InOrder inOrder = inOrder(analyticsService, analyticsEventMapper);
        inOrder.verify(analyticsEventMapper).toAnalyticsEvent(event);
        inOrder.verify(analyticsService).save(analyticsEvent);
    }
}