package faang.school.analytics.config.redis;

import faang.school.analytics.listener.like.LikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties propertiesConfig;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(
                propertiesConfig.getHost(),
                propertiesConfig.getPort());
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter likeEventAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likeEventAdapter, likeEventsTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter likeEventAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic likeEventsTopic() {
        return new ChannelTopic(propertiesConfig.getChannels().getLike_events());
    }
}
