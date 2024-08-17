package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostLikeEventListenerTest {

    @InjectMocks
    private PostLikeEventListener postLikeEventListener;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Spy
    private AnalyticsEventMapper mapper = new AnalyticsEventMapperImpl();

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Test
    void testOnMessage() {
        String channel = "like_topic";
        String body = "{\"postId\":2,\"postAuthorId\":1,\"actorId\":2,\"createdAt\":\"2024-08-14T07:51:38.4500143\"}";

        DefaultMessage message = new DefaultMessage(
                channel.getBytes(StandardCharsets.UTF_8),
                body.getBytes(StandardCharsets.UTF_8));

        postLikeEventListener.onMessage(message, channel.getBytes(StandardCharsets.UTF_8));

        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEvent.class));
    }
}