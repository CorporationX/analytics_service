package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.AdBoughtEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AdBoughtEventListener adBoughtEventListener;

    @Value("${redis.pubsub.topic:like-event}")
    private String likeEventTopic;

    @Value("${redis.pubsub.topic:adBought-event}")
    private String adBoughtTopic;

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new LikeEventListener(objectMapper, analyticsEventService, analyticsEventMapper));
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListener(), likeEventTopic());
        container.addMessageListener(new MessageListenerAdapter(adBoughtEventListener), adBoughtEventTopic());
        return container;
    }

    @Bean
    public ChannelTopic likeEventTopic() {
        return new ChannelTopic(likeEventTopic);
    }

    @Bean
    public ChannelTopic adBoughtEventTopic() {
        return new ChannelTopic(adBoughtTopic);
    }
}
