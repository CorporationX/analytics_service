package faang.school.analytics.redis.publisher;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChannelTopic profileViewChannel;

    public void publish(AnalyticsEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(profileViewChannel.getTopic(), json);
            log.info("Published analytics event: {}", json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize analytics event", e);
        }
    }
}
