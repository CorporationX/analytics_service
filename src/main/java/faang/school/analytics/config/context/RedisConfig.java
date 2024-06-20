package faang.school.analytics.config.context;

import faang.school.analytics.listener.FollowerEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channels.follower-event-channel.name}")
    private String followerTopicName;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        log.info("redis start work at port {}", port);
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(listenerAdapter, followerTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followerTopicName);
    }
}