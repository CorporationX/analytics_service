package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.listener.redis.LikeEventListener;
import faang.school.analytics.mapper.event.PostServiceEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    private ObjectMapper objectMapper;

    private LikeEventListener likeEventListener;

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private PostServiceEventMapper postServiceEventMapper;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        likeEventListener = new LikeEventListener(analyticsEventService, objectMapper, postServiceEventMapper, redisProperties);
    }

    @Test
    void test_onMessage_successful() throws JsonProcessingException {
        LocalDateTime date = LocalDateTime.now();

        LikeEvent expectedEvent = LikeEvent.builder()
                .postId(2L)
                .authorId(3L)
                .userId(1L)
                .createdAt(date)
                .build();

        AnalyticsEvent analyticsEvent = postServiceEventMapper.likeEventToAnalytics(expectedEvent);
        // Преобразуем объект в JSON, чтобы симулировать входящее сообщение
        String jsonMessage = objectMapper.writeValueAsString(expectedEvent);

        when(message.getBody()).thenReturn(jsonMessage.getBytes());

        likeEventListener.onMessage(message, null);

        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
        verifyNoMoreInteractions(analyticsEventService);
    }
}