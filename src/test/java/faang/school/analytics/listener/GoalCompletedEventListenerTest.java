package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;

    private GoalCompletedEvent goalCompletedEvent;
    private AnalyticsEvent analyticsEvent;
    private Message redisMessage;

    @BeforeEach
    public void setUp() {

        goalCompletedEvent = new GoalCompletedEvent(1L, 2L, LocalDateTime.now());
        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(2L)
                .actorId(1L)
                .eventType(EventType.GOAL_COMPLETED)
                .receivedAt(LocalDateTime.now())
                .build();

        byte[] messageBody = "{\"userId\":1,\"goalId\":2,\"completedAt\":\"2024-12-09T12:00:00\"}"
                .getBytes(StandardCharsets.UTF_8);
        redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(messageBody);
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        when(objectMapper.readValue(redisMessage.getBody(), GoalCompletedEvent.class)).thenReturn(goalCompletedEvent);
        when(analyticsEventMapper.goalCompletedEventToEntity(any(GoalCompletedEvent.class))).thenReturn(analyticsEvent);

        goalCompletedEventListener.onMessage(redisMessage, null);

        ArgumentCaptor<AnalyticsEvent> dtoCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventService).saveEvent(dtoCaptor.capture());
        AnalyticsEvent capturedEvent = dtoCaptor.getValue();
        assertEquals(EventType.GOAL_COMPLETED, capturedEvent.getEventType());
        assertEquals(goalCompletedEvent.goalId(), capturedEvent.getReceiverId());
        assertEquals(goalCompletedEvent.userId(), capturedEvent.getActorId());

        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    void testOnMessageIOException() throws IOException {
        when(objectMapper.readValue(redisMessage.getBody(), GoalCompletedEvent.class))
                .thenThrow(new IOException("Test Exception"));

        MessageProcessingException exception = assertThrows(MessageProcessingException.class, () ->
                goalCompletedEventListener.onMessage(redisMessage, null));

        assertEquals("Message parsing failed", exception.getMessage());
    }
}
