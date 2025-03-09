package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.AnalyticsServiceProperties;
import faang.school.analytics.queue.SearchAppearanceEventListener;
import faang.school.analytics.redis.AnalyticsMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final AnalyticsServiceProperties properties;

    @Bean
    public MessageListenerAdapter messageListener(AnalyticsMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    public MessageListenerAdapter searchEventListener(SearchAppearanceEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public RedisMessageListenerContainer premiumBoughtTopicRedisContainer(AnalyticsMessageSubscriber subscriber,
                                                                          ChannelTopic premiumBoughtTopic) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListener(subscriber), premiumBoughtTopic);
        return container;
    }

    @Bean
    public RedisMessageListenerContainer recommendationEventRedisContainer(
            MessageListenerAdapter messageListener, ChannelTopic recommendationEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListener, recommendationEventTopic);
        return container;
    }

    @Bean
    public RedisMessageListenerContainer filterUserEventRedisContainer(
            SearchAppearanceEventListener messageListener, ChannelTopic filterUserEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListener, filterUserEventTopic);
        return container;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return template;
    }

    @Bean("premiumBoughtTopic")
    public ChannelTopic premiumBoughtTopic() {
        return new ChannelTopic(properties.getRedis().getChannel().getBoughtPremiumTopic());
    }

    @Bean("recommendationEventTopic")
    public ChannelTopic recommendationEventTopic() {
        return new ChannelTopic(properties.getRedis().getChannel().getRecommendationEvent());
    }

    @Bean("filterUserEventTopic")
    public ChannelTopic filterUserEventTopic() {
        return new ChannelTopic(properties.getRedis().getChannel().getFilterUserEvent());
    }
}
