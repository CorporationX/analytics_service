package faang.school.analytics.config.redisConfig;

import faang.school.analytics.listeners.PostViewEventListener;
import org.springframework.beans.factory.annotation.Value;
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
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.PostViewEvent}")
    private String channelPostViewEvent;


    @Bean
    JedisConnectionFactory JedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration =
                new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfiguration);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(JedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    ChannelTopic PostViewTopic() {
        return new ChannelTopic(channelPostViewEvent);
    }

    @Bean
    MessageListenerAdapter postViewListenerAdapter(
            PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(
            MessageListenerAdapter postViewListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(JedisConnectionFactory());
        container.addMessageListener(postViewListenerAdapter,PostViewTopic());
        return container;
    }
}
