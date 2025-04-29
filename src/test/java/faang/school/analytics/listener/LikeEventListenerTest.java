package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.LikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.until.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {

    @InjectMocks
    private LikeEventListener likeEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    private final   String json = """
                    {
                        "postId": 1,
                        "authorId": 2,
                        "userId": 3,
                        "likedAt": "2024-04-01T12:00:00",
                        "type": "LIKE"
                    }
                """;
    private final LikeEvent event = LikeEvent.builder()
            .postId(1L)
            .authorId(1L)
            .userId(1L)
            .likedAt(LocalDateTime.parse("2024-04-01T12:00:00"))
            .type(EventType.LIKED_POST)
            .build();

    @Test
    void onMessage_shouldHandleLikeEvent_whenMessageIsValid() throws Exception{

        when(message.getBody()).thenReturn(json.getBytes((StandardCharsets.UTF_8)));
        when(objectMapper.readValue(any(byte[].class),eq(LikeEvent.class))).thenReturn(event);

        likeEventListener.onMessage(message, "liked_post_topic".getBytes(StandardCharsets.UTF_8));

        verify(analyticsEventService, times(1)).handleLikeEvent(event);
    }

    @Test
    void onMessage_shouldThrowRuntimeException_whenJsonParsingFails() throws Exception{
        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(any(byte[].class), eq(LikeEvent.class))).thenThrow(new IOException());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () ->likeEventListener.onMessage(message, "liked_post_topic".getBytes(StandardCharsets.UTF_8)));

        assertTrue(exception.getMessage().contains("Ошибка обработки сообщения Redis"));
    }

}
