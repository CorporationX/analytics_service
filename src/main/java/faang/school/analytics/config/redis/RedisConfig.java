package faang.school.analytics.config.redis;

import faang.school.analytics.listener.FollowerEventListener;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfig {
    private int port;
    private String host;

    @Value("${spring.data.redis.channel.follower-event-channel}")
    private String followerEventTopic;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration =
                new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    MessageListenerAdapter followerListener(FollowerEventListener goalCompletedListener) {
        return new MessageListenerAdapter(goalCompletedListener);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(FollowerEventListener followerListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerEventTopic());

        return container;
    }

    @Bean
    ChannelTopic followerEventTopic() {
        return new ChannelTopic(followerEventTopic);
    }
}
