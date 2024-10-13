package faang.school.analytics.config.redis;

import faang.school.analytics.listener.like.LikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final LikeEventListener likeEventsListener;
    private final RedisPropertiesConfiguration propertiesConfig;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(propertiesConfig.getHost()
                , propertiesConfig.getPort());
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likeEventListenerAdapter(likeEventsListener), likeEventsTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter likeEventListenerAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic likeEventsTopic() {
        return new ChannelTopic(propertiesConfig.getLikeEvents());
    }
}
