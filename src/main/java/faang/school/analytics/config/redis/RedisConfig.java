package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.postview.PostViewEventListener;
import faang.school.analytics.service.analytic.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.post-view}")
    private String postViewTopicName;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration connectionConfig = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(connectionConfig);
    }

    @Bean
    public ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewTopicName);
    }

    @Bean
    public PostViewEventListener postViewEventListener() {
        return new PostViewEventListener(objectMapper, analyticsEventService);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(postViewEventListener(), postViewTopic());
        return container;
    }
}
