package faang.school.analytics.eventlistner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.mapper.goalcompleted.UserServiceEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private UserServiceEventMapper userServiceEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private GoalCompletedEventListener listener;

    @Test
    void onMessage_shouldParseAndSaveEvent() throws IOException {
        LocalDateTime time = LocalDateTime.of(2025, Month.JUNE, 16, 12, 0, 0);
        GoalCompletedEvent event = new GoalCompletedEvent(1L, "goalName", time); // adjust constructor as needed
        AnalyticsEvent mappedEvent = new AnalyticsEvent(); // dummy mapped object

        byte[] json = "{\"goalId\":1,\"goalName\":\"goalName\",\"userId\":2,\"ownerId\":3}".getBytes();
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(json);

        when(objectMapper.readValue(eq(json), eq(GoalCompletedEvent.class))).thenReturn(event);
        when(userServiceEventMapper.goalCompleteToAnalytics(event)).thenReturn(mappedEvent);

        listener.onMessage(message, null);

        verify(objectMapper).readValue(eq(json), eq(GoalCompletedEvent.class));
        verify(userServiceEventMapper).goalCompleteToAnalytics(event);
        verify(analyticsEventService).saveEvent(mappedEvent);
    }

    @Test
    void onMessage_shouldThrowRuntimeException_whenJsonIsInvalid() throws IOException {
        byte[] invalidJson = "not-a-json".getBytes();

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(invalidJson);

        when(objectMapper.readValue(eq(invalidJson), eq(GoalCompletedEvent.class)))
                .thenThrow(new IOException("Failed to parse"));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                listener.onMessage(message, null)
        );

        assertEquals("Analytics write exception", ex.getMessage());
    }
}
