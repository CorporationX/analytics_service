package faang.school.analytics.config.redis;

import faang.school.analytics.listener.SearchAppearanceEventListener;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
public class RedisConfiguration {
    private final SearchAppearanceEventListener searchAppearanceEventListener;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.search-appearance.name}")
    private String searchAppearanceTopic;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Created redis connection factory with host: {}, port: {}", host, port);
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfiguration);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(JedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(searchAppearanceEventListener);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic(searchAppearanceTopic));

        return container;
    }
}