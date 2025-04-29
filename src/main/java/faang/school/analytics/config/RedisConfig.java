package faang.school.analytics.config;

import faang.school.analytics.dto.event.LikeEvent;
import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig =
                new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());

        JedisConnectionFactory factory = new JedisConnectionFactory(redisConfig);
        factory.afterPropertiesSet();
        log.info("Created JedisConnectionFactory with host {} and port {}, Redis connection status: {}",
                factory.getHostName(), factory.getPort(), factory.getConnection().isClosed() ? "DISCONNECTION" : "CONNECTION");
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(LikeEvent.class));
        log.info("Initialized RedisTemplate with EventDto serializer");
        return template;
    }

    @Bean
    public List<ChannelTopic> eventTopics() {
        List<ChannelTopic> topics = redisProperties.getTopics().values().stream()
                .map(ChannelTopic::new)
                .toList();

        log.info("Subscribe to Redis topics: {}", topics.stream().map(ChannelTopic::getTopic).toList());
        return topics;
    }

    @Bean
    MessageListenerAdapter likeEventListenerAdapter(LikeEventListener likeEventListener){
        return new MessageListenerAdapter(likeEventListener, "onMessage");
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter likeEventListenerAdapter){
        RedisMessageListenerContainer redisContainer = new RedisMessageListenerContainer();
        redisContainer.setConnectionFactory(jedisConnectionFactory());
        redisContainer.addMessageListener(likeEventListenerAdapter, eventTopics());
        return redisContainer;
    }
}
