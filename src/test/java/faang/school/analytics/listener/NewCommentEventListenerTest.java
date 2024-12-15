package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.event.NewCommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
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
class NewCommentEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private NewCommentEventListener newCommentEventListener;

    private String json;
    private Message message;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        json = "{\"postId\": 1, \"authorId\": 2, \"commentId\": 3, \"createdAt\": \"2024-12-13T12:00:00\"}";
        message = mock(Message.class);
        analyticsEvent = mock(AnalyticsEvent.class);
    }

    @Test
    @DisplayName("Test deserialization of JSON message")
    void testDeserializationOfJsonMessage() throws Exception {
        ObjectMapper realObjectMapper = new ObjectMapper();
        realObjectMapper.registerModule(new JavaTimeModule());

        NewCommentEvent result = realObjectMapper.readValue(json.getBytes(), NewCommentEvent.class);

        assertNotNull(result);
        assertEquals(1, result.getPostId());
        assertEquals(2, result.getAuthorId());
        assertEquals(3, result.getCommentId());
        assertEquals(LocalDateTime.class, result.getCreatedAt().getClass());
        assertEquals(LocalDateTime.of(2024, 12, 13, 12, 0), result.getCreatedAt());
    }

    @Test
    @DisplayName("Event handled and saved success")
    void testOnMessage_Success() throws Exception {
        NewCommentEvent event = new NewCommentEvent(1L, 2L, 3L, LocalDateTime.of(2024, 12, 13, 12, 0));

        when(message.getBody()).thenReturn(json.getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(NewCommentEvent.class))).thenReturn(event);
        when(analyticsEventMapper.newCommentEventToEntity(event)).thenReturn(analyticsEvent);

        newCommentEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(NewCommentEvent.class));
        verify(analyticsEventMapper, times(1)).newCommentEventToEntity(event);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }

    @Test
    @DisplayName("Event handled and saved fail: deserialization error")
    void testOnMessage_Fail_DeserializationError() throws Exception {

        when(message.getBody()).thenReturn(json.getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(NewCommentEvent.class))).thenThrow(new IOException("Deserialization error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> newCommentEventListener.onMessage(message, null));
        assertEquals("Error while deserializing", ex.getMessage());

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(NewCommentEvent.class));
        verify(analyticsEventMapper, never()).newCommentEventToEntity(any(NewCommentEvent.class));
        verify(analyticsEventService, never()).saveEvent(any(AnalyticsEvent.class));

    }


}