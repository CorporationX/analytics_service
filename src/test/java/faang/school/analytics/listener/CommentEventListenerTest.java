package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.JsonTestHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private CommentEventListener commentEventListener;

    private String json;
    private Message message;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        json = JsonTestHandler.readJsonFileToString("/json/new_comment_event.json");
        message = mock(Message.class);
        analyticsEvent = mock(AnalyticsEvent.class);
    }

    @Test
    @DisplayName("Test deserialization of JSON message")
    void testDeserializationOfJsonMessage() throws Exception {
        ObjectMapper realObjectMapper = new ObjectMapper();
        realObjectMapper.registerModule(new JavaTimeModule());

        CommentEvent result = realObjectMapper.readValue(json.getBytes(), CommentEvent.class);

        assertNotNull(result);
        assertEquals(1, result.getCommentAuthorId());
        assertEquals(2, result.getPostAuthorId());
        assertEquals(3, result.getPostId());
        assertEquals(4, result.getCommentId());
        assertEquals(LocalDateTime.class, result.getCreatedAt().getClass());
        assertEquals(LocalDateTime.of(2024, 12, 13, 12, 0), result.getCreatedAt());
    }

    @Test
    @DisplayName("Event handled and saved success")
    void testOnMessage_Success() throws Exception {
        CommentEvent event = new CommentEvent(1L, 2L, 3L, 4L, LocalDateTime.of(2024, 12, 13, 12, 0));

        when(message.getBody()).thenReturn(json.getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class))).thenReturn(event);
        when(analyticsEventMapper.newCommentEventToEntity(event)).thenReturn(analyticsEvent);

        commentEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(CommentEvent.class));
        verify(analyticsEventMapper, times(1)).newCommentEventToEntity(event);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }

    @Test
    @DisplayName("Event handled and saved fail: deserialization error")
    void testOnMessage_Fail_DeserializationError() throws Exception {

        when(message.getBody()).thenReturn(json.getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class))).thenThrow(new IOException("Deserialization error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> commentEventListener.onMessage(message, null));
        assertEquals("Error while deserializing", ex.getMessage());

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(CommentEvent.class));
        verify(analyticsEventMapper, never()).newCommentEventToEntity(any(CommentEvent.class));
        verify(analyticsEventService, never()).saveEvent(any(AnalyticsEvent.class));

    }


}