package faang.school.analytics.messaging.listener.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.postview.PostViewEventDto;
import faang.school.analytics.exception.event.DataTransformationException;
import faang.school.analytics.mapper.postview.PostViewEventMapper;
import faang.school.analytics.messaging.listener.postview.PostViewEventListener;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.connection.Message;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PostEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Message redisMessage;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private PostViewEventMapper postViewEventMapper;
    @InjectMocks
    private PostViewEventListener postViewEventListener;

    @Test
    void onMessage_shouldHandleEventAndSaveToDatabase() throws IOException {
        UUID eventId = UUID.randomUUID();
        var postViewEvent = PostViewEventDto.builder()
                .postId(1L)
                .authorId(1L)
                .userId(2L)
                .timestamp(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();
        var analyticsEvent = AnalyticsEvent.builder()
                .eventId(eventId)
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();
        String jsonMessage = String.format(
                "{\"eventId\":\"%s\",\"receiverId\":1,\"requesterId\":2,\"timestamp\":\"2021-01-01T00:00:00\"}",
                eventId
        );

        when(redisMessage.getBody()).thenReturn(jsonMessage.getBytes());
        when(objectMapper.readValue(jsonMessage.getBytes(), PostViewEventDto.class)).thenReturn(postViewEvent);
        when(postViewEventMapper.toEntity(postViewEvent)).thenReturn(analyticsEvent);

        postViewEventListener.onMessage(redisMessage, new byte[0]);

        verify(objectMapper).readValue(jsonMessage.getBytes(), PostViewEventDto.class);
        verify(analyticsEventService).saveEvent(analyticsEvent);

        assertEquals(EventType.POST_VIEW, analyticsEvent.getEventType());
    }

    @Test
    void onMessage_shouldHandleException() throws Exception {
        String invalidJsonMessage = "invalid json";
        when(redisMessage.getBody()).thenReturn(invalidJsonMessage.getBytes());
        when(objectMapper.readValue(invalidJsonMessage.getBytes(), PostViewEventDto.class))
                .thenThrow(new IOException("Invalid JSON"));

        assertThrows(DataTransformationException.class, () -> postViewEventListener.onMessage(redisMessage, new byte[0]));
        verifyNoInteractions(analyticsEventService, postViewEventMapper);
    }
}