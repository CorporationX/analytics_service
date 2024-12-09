package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.event.GoalCompletedEventMapper;
import faang.school.analytics.mapper.event.GoalCompletedEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.event.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {
    @Spy
    private GoalCompletedEventMapperImpl goalCompletedEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GoalCompletedEventListener listener;

    private GoalCompletedEvent goalCompletedEvent;
    private String messageString;
    @Mock
    private Message message;
    String jsonRepresentation = "{"
            + "\"userId\": 1,"
            + "\"goalId\": 123,"
            + "\"goalAchieveDateTime\": \"2023-10-10T15:30:00\""
            + "}";

    @BeforeEach
    void setUp() {
        goalCompletedEvent = new GoalCompletedEvent();
        goalCompletedEvent.setUserId(1L);
        goalCompletedEvent.setGoalId(2L);

    }

    @Test
    void testHandleEventShouldMapAndAddAnalyticsEvent() {
        goalCompletedEvent.setGoalAchieveDateTime(LocalDateTime.now());

        listener.handleEvent(goalCompletedEvent);

        verify(analyticsEventService).addNewEvent(any(AnalyticsEvent.class));
    }

    @Test
    void testOnMessage_ShouldProcessMessage() throws com.fasterxml.jackson.core.JsonProcessingException {
        when(message.getBody()).thenReturn(jsonRepresentation.getBytes());
        when(objectMapper.readValue(jsonRepresentation, GoalCompletedEvent.class)).thenReturn(goalCompletedEvent);

        listener.onMessage(message, null);

        verify(goalCompletedEventMapper).toEntity(goalCompletedEvent);
        verify(analyticsEventService).addNewEvent(any(AnalyticsEvent.class));
    }

}