package faang.school.analytics.publish.listener.like;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.dto.event.type.service.post.like.PostLikeEventDto;
import faang.school.analytics.mapper.LikeMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class PostLikeEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final LikeMapper likeMapper;
    private final ObjectMapper objectMapper;
    private final RedisProperties redisProperties;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostLikeEventDto postLikeEventDto = objectMapper.readValue(message.getBody(), PostLikeEventDto.class);
            analyticsEventService.saveEvent(likeMapper.toAnalyticsEvent(postLikeEventDto));
        } catch (JsonMappingException e) {
            log.error("JSON mapping error: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Error while parsing message: {}", message.getBody(), e);
            throw new MessageProcessingException("Failed to process message", e);
        }
        log.info("{}: Message saved: {}", redisProperties.getPostLikeEventChannelName(), message.getBody());
    }
}
