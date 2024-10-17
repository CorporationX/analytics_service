package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticsevents.AnalyticsEventMapper;
import faang.school.analytics.model.event.FollowerEvent;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    private Message message;
    private FollowerEvent followerEventDto;

    @BeforeEach
    void setUp() {
        followerEventDto = FollowerEvent.builder().build();
        String json = "{\"followerId\":1, \"followeeId\":2}";
        message = mock(Message.class);
        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void onMessage_shouldHandleEventSuccessfully() throws IOException {
        // given
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class))).thenReturn(followerEventDto);
        when(analyticsEventMapper.toEntity(followerEventDto)).thenReturn(analyticsEvent);
        // when
        followerEventListener.onMessage(message, null);
        // then
        verify(analyticsEventMapper, times(1)).toEntity(followerEventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
        verify(analyticsEventService).saveEvent(argThat(event -> event.getEventType() == EventType.FOLLOWER));
    }

    @Test
    void onMessage_shouldThrowRuntimeException_whenDeserializationFails() throws IOException {
        // given
        when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class)))
                .thenThrow(new JsonProcessingException("Test exception") {});
        // when & then
        assertThrows(RuntimeException.class, () -> followerEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).saveEvent(any());
    }
}