package faang.school.analytics.config;

import faang.school.analytics.listener.PostViewEventListener;
import faang.school.analytics.listener.RecommendationListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.recommendation}")
    private String recommendationChannel;
    @Value("${spring.data.redis.channels.post_view}")
    private String postViewChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Crated redis connection factory with host: {}, port: {}", host, port);
        return new JedisConnectionFactory();
    }

    @Bean
    public MessageListenerAdapter recommendationAdapter(RecommendationListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter postViewAdapter(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    public ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter recommendationAdapter,
                                                        MessageListenerAdapter postViewAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recommendationAdapter, recommendationTopic());
        container.addMessageListener(postViewAdapter, postViewTopic());
        return container;
    }
}
