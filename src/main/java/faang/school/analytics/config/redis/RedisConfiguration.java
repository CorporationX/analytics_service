package faang.school.analytics.config.redis;

import faang.school.analytics.listener.SearchAppearanceEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final SearchAppearanceEventListener searchAppearanceEventListener;
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.channels.search-appearance.name}")
    private String searchAppearanceTopic;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfiguration);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(searchAppearanceEventListener);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic(searchAppearanceTopic));

        return container;
    }
}