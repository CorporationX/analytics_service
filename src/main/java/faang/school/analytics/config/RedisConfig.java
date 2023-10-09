package faang.school.analytics.config;

import faang.school.analytics.listeners.PostViewEventListener;
import faang.school.analytics.listeners.RecomendationEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.post-view}")
    private String postViewTopic;
    @Value("${spring.data.redis.channel.recommendation_view}")
    private String recomendationChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(config);

    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    MessageListenerAdapter goalCompletedListener(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    MessageListenerAdapter recomendationListener(RecomendationEventListener recomendationEventListener) {
        return new MessageListenerAdapter(recomendationEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(PostViewEventListener postViewEventListener,
                                                 RecomendationEventListener recomendationEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(goalCompletedListener(postViewEventListener), goalCompletedTopic());
        container.addMessageListener(recomendationListener(recomendationEventListener), recomendationTopic());
        return container;
    }


    @Bean
    ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(postViewTopic);
    }

    @Bean
    ChannelTopic recomendationTopic() {
        return new ChannelTopic(recomendationChannel);
    }
}
