package faang.school.analytics.publish.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.model.event.type.service.post.like.PostLikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class PostLikeEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;
    private final RedisProperties redisProperties;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostLikeEvent postLikeEvent = objectMapper.readValue(message.getBody(), PostLikeEvent.class);
            analyticsEventService.saveEvent(postLikeEvent.createAnalyticsEvent());

        } catch (IOException e) {
            log.error("Error while parsing message: {}", message.getBody(), e);
            throw new MessageProcessingException("Failed to process message", e);
        }
        log.info("{}: Message saved: {}", redisProperties.getPostLikeEventChannelName(), message.getBody());
    }
}
