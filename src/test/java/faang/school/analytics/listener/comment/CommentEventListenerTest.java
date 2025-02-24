package faang.school.analytics.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import java.io.IOException;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CommentEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Message message;

    @InjectMocks
    private CommentEventListener commentEventListener;

    private CommentEvent commentEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        commentEvent = CommentEvent.builder()
                .postAuthorId(1L)
                .commentAuthorId(2L)
                .postId(3L)
                .commentId(4L)
                .commentedAt(LocalDateTime.of(2023, 10, 1, 12, 0))
                .build();

        analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setEventType(EventType.POST_COMMENT);
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        String json = "{\"postAuthorId\":1,\"commentAuthorId\":2,\"postId\":3,\"commentId\":4,\"commentedAt\":\"2023-10-01T12:00:00\"}";
        byte[] jsonBytes = json.getBytes();

        when(message.getBody()).thenReturn(jsonBytes);
        when(objectMapper.readValue(jsonBytes, CommentEvent.class)).thenReturn(commentEvent);
        when(analyticsEventMapper.toAnalyticsEvent(commentEvent)).thenReturn(analyticsEvent);

        commentEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(jsonBytes, CommentEvent.class);
        verify(analyticsEventMapper, times(1)).toAnalyticsEvent(commentEvent);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }

    @Test
    void testOnMessageIOException() throws IOException {
        String json = "invalid-json";
        byte[] jsonBytes = json.getBytes();

        when(message.getBody()).thenReturn(jsonBytes);
        doThrow(IOException.class).when(objectMapper).readValue(jsonBytes, CommentEvent.class);

        assertThrows(RuntimeException.class, () -> commentEventListener.onMessage(message, null));

        verify(objectMapper, times(1)).readValue(jsonBytes, CommentEvent.class);
        verify(analyticsEventMapper, never()).toAnalyticsEvent(any());
        verify(analyticsEventService, never()).saveEvent(any());
    }
}
