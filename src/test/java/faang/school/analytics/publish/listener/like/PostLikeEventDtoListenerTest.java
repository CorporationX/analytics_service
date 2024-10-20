package faang.school.analytics.publish.listener.like;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.event.type.service.post.like.PostLikeEventDto;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.LikeMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostLikeEventDtoListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private LikeMapper likeMapper;

    @InjectMocks
    private PostLikeEventListener postLikeEventListener;

    @Mock
    private Message message;

    @Test
    void testOnMessageProcessesValidEvent() throws Exception {
        String jsonMessage = "{\"type\":\"like\",\"data\":\"someData\"}";
        PostLikeEventDto postLikeEventDto = new PostLikeEventDto(1L, 2L, 3L, LocalDateTime.now());
        when(message.getBody()).thenReturn(jsonMessage.getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(PostLikeEventDto.class))).thenReturn(postLikeEventDto);
        when(likeMapper.toAnalyticsEvent(postLikeEventDto)).thenReturn(LikeMapper.INSTANCE.toAnalyticsEvent(postLikeEventDto));
        when(redisProperties.getPostLikeEventChannelName()).thenReturn("testChannel");

        postLikeEventListener.onMessage(message, null);

        verify(analyticsEventService).saveEvent(LikeMapper.INSTANCE.toAnalyticsEvent(postLikeEventDto));
        verify(redisProperties).getPostLikeEventChannelName();

    }

    @Test
    void testOnMessageHandlesJsonMappingException() throws Exception {
        String invalidJsonMessage = "{\"type\":\"like\"";
        when(message.getBody()).thenReturn(invalidJsonMessage.getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(PostLikeEventDto.class)))
                .thenThrow(new MessageProcessingException("Failed to map JSON to PostLikeEventDto", new Exception()));

        MessageProcessingException exception = assertThrows(MessageProcessingException.class, () -> {
            postLikeEventListener.onMessage(message, null);
        });

        assertEquals("Failed to map JSON to PostLikeEventDto", exception.getMessage());
        verify(analyticsEventService, never()).saveEvent(any());
    }

}
