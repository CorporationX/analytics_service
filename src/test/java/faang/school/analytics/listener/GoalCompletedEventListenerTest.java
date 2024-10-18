package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.GoalCompletedEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Spy
    private AnalyticsEventMapper mapper;

    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;

    @Test
    void testOnMessage_Success() throws Exception {
        Long goalId = 1L;
        Long userId = 2L;
        GoalCompletedEvent goalCompletedEvent = new GoalCompletedEvent (goalId, userId);

        String messageBody = "{\"goalId\":" + goalId + ", \"userId\":" + userId + "\"}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        when(objectMapper.readValue(any(byte[].class), eq(GoalCompletedEvent.class))).thenReturn(goalCompletedEvent);

        goalCompletedEventListener.onMessage(message, null);

        ArgumentCaptor<AnalyticsEvent> eventCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(eventCaptor.capture());

        AnalyticsEvent savedEvent = eventCaptor.getValue();
        assertEquals(goalId, savedEvent.getReceiverId());
        assertEquals(userId, savedEvent.getActorId());
        assertEquals(EventType.GOAL_COMPLETED, savedEvent.getEventType());
    }

    @Test
    void testOnMessage_throwException() throws Exception {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("invalid".getBytes());

        when(objectMapper.readValue(any(byte[].class), eq(GoalCompletedEvent.class))).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> goalCompletedEventListener.onMessage(message, null));
        verify(objectMapper, times(1)).readValue(message.getBody(), GoalCompletedEvent.class);
        verifyNoMoreInteractions(mapper, analyticsEventService);
    }
}