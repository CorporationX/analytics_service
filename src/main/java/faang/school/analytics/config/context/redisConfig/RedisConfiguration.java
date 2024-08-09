package faang.school.analytics.config.context.redisConfig;

import faang.school.analytics.redisListener.EventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;
    private final EventListener<?> followerEventListener;

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfig =
                new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        return new JedisConnectionFactory(standaloneConfig);
    }

    @Bean(name = "followerViewTopic")
    public ChannelTopic followerViewTopic() {
        return new ChannelTopic(redisProperties.getChannel().getFollowerView());
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory());
        redisMessageListenerContainer.addMessageListener(messageListener(), followerViewTopic());

        return redisMessageListenerContainer;
    }
}
