package faang.school.analytics.config.redis;

import faang.school.analytics.redis.listener.CommentEventListener;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Setter
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    private final CommentEventListener commentEventListener;

    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        return new JedisConnectionFactory(config);
    }

    @Bean
    public MessageListenerAdapter commentEventListenerAdapter() {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        var container = new RedisMessageListenerContainer();

        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(commentEventListenerAdapter(), commentTopic());

        return container;
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(redisProperties.getChannels().getCommentsChannel());
    }
}
