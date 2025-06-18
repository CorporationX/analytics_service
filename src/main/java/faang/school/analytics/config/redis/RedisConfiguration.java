package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.RecommendationEventListener;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RedisConfiguration {
    private final RedisProperties redisProperties;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory, MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(adapter, recommendationTopic());

        return container;
    }

    @Bean
    MessageListenerAdapter recommendationListener(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(redisProperties.channelNames().recommendationEvent());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory,
                                                       ObjectMapper objectMapper) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}