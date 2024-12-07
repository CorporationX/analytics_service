package faang.school.analytics.config.redis;

import faang.school.analytics.event.listener.LikeEventListener;
import faang.school.analytics.event.publisher.AnalyticsEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public MessageListenerAdapter messageListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener, "onMessage");
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        MessageListenerAdapter messageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListener, topic());
        return container;
    }

    @Bean
    public AnalyticsEventPublisher redisPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        return new AnalyticsEventPublisher(redisTemplate, topic);
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("analyticsEventTopic");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
