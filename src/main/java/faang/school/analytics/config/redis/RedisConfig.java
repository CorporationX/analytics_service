package faang.school.analytics.config.redis;

import faang.school.analytics.listener.AnalyticsEventListener;
import faang.school.analytics.listener.CommentEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.channel.analytics}")
    private String analyticsChannel;

    @Value("${spring.data.redis.channel.comment-analytics}")
    private String commentAnalyticsTopicName;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setDefaultSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAnalyticsEventAdapter,
            ChannelTopic analyticsTopic,
            MessageListenerAdapter commentEventListenerAdapter,
            ChannelTopic commentEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAnalyticsEventAdapter, analyticsTopic);
        container.addMessageListener(commentEventListenerAdapter, commentEventTopic);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAnalyticsEventAdapter(AnalyticsEventListener analyticsEventListener) {
        return new MessageListenerAdapter(analyticsEventListener);
    }

    @Bean
    public ChannelTopic analyticsTopic() {
        return new ChannelTopic(analyticsChannel);
    }

    @Bean
    public MessageListenerAdapter commentEventListenerAdapter(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener, "onMessage");
    }

    @Bean
    public ChannelTopic commentEventTopic() {
        return new ChannelTopic(commentAnalyticsTopicName);
    }
}