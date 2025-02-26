package faang.school.analytics.config;

import faang.school.analytics.message.RecommendationEventListener;
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
public class RedisConfig {

    private final RedisConfigurationProperties redisConfigurationProperties;
    private final RedisChannels redisChannels;
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisConfigurationProperties
                .getHost(), redisConfigurationProperties.getPort());
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    MessageListenerAdapter recommendationListener(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    ChannelTopic redisRecommendationEventTopic() {
        return new ChannelTopic(redisChannels.getRecommendationChannel());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter recommendationListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recommendationListener, redisRecommendationEventTopic());
        return container;
    }

}
