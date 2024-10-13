package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticsevents.AnalyticsEventMapper;
import faang.school.analytics.model.dto.GoalCompletedEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.impl.analyticsevent.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;

    private Message message;
    private GoalCompletedEventDto goalCompletedEventDto;

    @BeforeEach
    void setUp() {
        goalCompletedEventDto = GoalCompletedEventDto.builder().build();
        String json = "{\"userId\":1, \"goalId\":2}";
        message = mock(Message.class);
        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void onMessage_shouldHandleEventSuccessfully() throws IOException {
        // given
        var analyticsEvent = new AnalyticsEvent();
        when(objectMapper.readValue(any(byte[].class), eq(GoalCompletedEventDto.class))).thenReturn(goalCompletedEventDto);
        when(analyticsEventMapper.toEntity(goalCompletedEventDto)).thenReturn(analyticsEvent);
        // when
        goalCompletedEventListener.onMessage(message, null);
        // then
        verify(analyticsEventMapper, times(1)).toEntity(goalCompletedEventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
        verify(analyticsEventService).saveEvent(argThat(event -> event.getEventType() == EventType.GOAL_COMPLETED));
    }

    @Test
    void onMessage_shouldThrowRuntimeException_whenDeserializationFails() throws IOException {
        // given
        when(objectMapper.readValue(any(byte[].class), eq(GoalCompletedEventDto.class)))
                .thenThrow(new JsonProcessingException("Test exception") {
                });
        // when & then
        assertThrows(RuntimeException.class, () -> goalCompletedEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).saveEvent(any());
    }
}