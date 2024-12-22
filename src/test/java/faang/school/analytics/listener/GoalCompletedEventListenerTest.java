package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.GoalCompletedMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Spy
    private GoalCompletedMapper goalCompletedMapper;

    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;

    private GoalCompletedEvent goalCompletedEvent;
    private AnalyticsEvent analyticsEvent;
    private AnalyticsEventResponseDto analyticsEventResponseDto;

    @BeforeEach
    public void setUp() {
        goalCompletedEvent = new GoalCompletedEvent(1L, 2L, LocalDateTime.now());
        analyticsEventResponseDto = AnalyticsEventResponseDto.builder()
                .receiverId(2L)
                .actorId(1L)
                .eventType(EventType.GOAL_COMPLETED)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        String messageBody = "{\"goalId\": 1, \"userId\": 42, \"completedAt\": \"2024-12-14T10:15:30\"}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        GoalCompletedEvent goalCompletedEvent = new GoalCompletedEvent(1L, 42L, LocalDateTime.now());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        lenient().when(objectMapper.readValue(messageBody.getBytes(), GoalCompletedEvent.class)).thenReturn(goalCompletedEvent);
        lenient().when(goalCompletedMapper.toEntity(goalCompletedEvent)).thenReturn(analyticsEvent);

        goalCompletedEventListener.onMessage(message, null);

        verify(analyticsEventService).saveEvent(analyticsEvent);
        assertEquals(EventType.GOAL_COMPLETED, analyticsEvent.getEventType());
    }

    @Test
    void testOnMessageIOException() throws IOException {
        Message redisMessage = mock(Message.class);
        byte[] messageBody = "{\"goalId\": 1, \"userId\": 42, \"completedAt\": \"2024-12-14T10:15:30\"}".getBytes();
        when(redisMessage.getBody()).thenReturn(messageBody);

        when(objectMapper.readValue(redisMessage.getBody(), GoalCompletedEvent.class))
                .thenThrow(new IOException("Test Exception"));

        MessageProcessingException exception = assertThrows(MessageProcessingException.class, () ->
                goalCompletedEventListener.onMessage(redisMessage, null));

        assertEquals("Message parsing failed", exception.getMessage());
    }
}
