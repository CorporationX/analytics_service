package faang.school.analytics.listener;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventServiceImpl;

@ExtendWith(MockitoExtension.class)
class RecommendationEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper eventMapper;

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @InjectMocks
    private RecommendationEventListener listener;

    private Message MESSAGE = new DefaultMessage("{\"id\":1}".getBytes(), "topic".getBytes());

    @Test
    @DisplayName("Should process valid message and save event")
    void onMessage_ValidMessage_ShouldSaveEvent() throws IOException {
        long COMMENT_AUTHOR_ID = 10L;
        long POST_AUTHOR_ID = 20L;
        LocalDateTime CREATED_AT = LocalDateTime.now();

        CommentEvent event = new CommentEvent(POST_AUTHOR_ID, COMMENT_AUTHOR_ID, CREATED_AT);
        CreateAnalyticsEventDto analyticsDto = CreateAnalyticsEventDto.builder()
            .receiverId(POST_AUTHOR_ID)
            .actorId(COMMENT_AUTHOR_ID)
            .eventType(EventType.POST_COMMENT)
            .receivedAt(CREATED_AT)
            .build();

        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class))).thenReturn(event);

        listener.onMessage(MESSAGE, null);
        verify(analyticsEventService, times(1)).saveEvent(analyticsDto);
    }

    @Test
    @DisplayName("Should throw exception when JSON is invalid")
    void onMessage_InvalidJson_ShouldThrowException() throws IOException {
        byte[] body = "invalid-json".getBytes();

        when(MESSAGE.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, CommentEvent.class)).thenThrow(IOException.class);

        assertThrows(MessageProcessingException.class, () -> listener.onMessage(MESSAGE, null));
        verify(analyticsEventService, never()).saveEvent(any());
    }
}
