package faang.school.analytics.event.publisher;

import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsEventPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    public AnalyticsEventPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(AnalyticsEvent event) {
        redisTemplate.convertAndSend(topic.getTopic(), event);
    }
}