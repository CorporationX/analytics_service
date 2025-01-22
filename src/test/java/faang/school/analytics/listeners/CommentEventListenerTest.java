package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.AnalyticsMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CommentEventListenerTest {

    @Mock
    private AnalyticsMapper analyticsMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsService analyticsEventService;

    @InjectMocks
    private CommentEventListener commentEventListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void onMessage_ShouldProcessMessageSuccessfully() throws IOException {
        String jsonMessage = """
                    {
                        "postId": 1,
                        "authorPostId": 2,
                        "authorCommentId": 3,
                        "commentId": 4,
                        "createdAt": "2023-01-20T10:15:30"
                    }
                """;
        byte[] messageBody = jsonMessage.getBytes();
        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(messageBody);

        CommentEvent commentEvent = new CommentEvent(
                1L,
                2L,
                3L,
                4L,
                LocalDateTime.of(2023, 1, 20, 10, 15, 30)
        );
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        when(objectMapper.readValue(messageBody, CommentEvent.class)).thenReturn(commentEvent);
        when(analyticsMapper.toAnalyticsEvent(commentEvent)).thenReturn(analyticsEvent);
        when(analyticsEventService.addAnalyticsEvent(analyticsEvent)).thenReturn(analyticsEvent);

        commentEventListener.onMessage(redisMessage, null);

        verify(objectMapper, times(1)).readValue(messageBody, CommentEvent.class);
        verify(analyticsMapper, times(1)).toAnalyticsEvent(commentEvent);
        verify(analyticsEventService, times(1)).addAnalyticsEvent(analyticsEvent);
    }

    @Test
    void onMessage_ShouldThrowRuntimeException_WhenIOExceptionOccurs() throws IOException {
        String jsonMessage = """
                    {
                        "postId": 1,
                        "authorPostId": 2,
                        "authorCommentId": 3,
                        "commentId": 4,
                        "createdAt": "2023-01-20T10:15:30"
                    }
                """;
        byte[] messageBody = jsonMessage.getBytes();
        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, CommentEvent.class)).thenThrow(new IOException("Invalid JSON"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                commentEventListener.onMessage(redisMessage, null)
        );

        assertEquals("java.io.IOException: Invalid JSON", exception.getMessage());
        verify(objectMapper, times(1)).readValue(messageBody, CommentEvent.class);
        verifyNoInteractions(analyticsMapper);
        verifyNoInteractions(analyticsEventService);
    }
}
