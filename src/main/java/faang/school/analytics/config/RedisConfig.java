package faang.school.analytics.config;

import faang.school.analytics.dto.event.LikeEvent;
import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.properties.RedisProperties;
import faang.school.analytics.until.EventType;
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
        log.info("Initialized RedisTemplate with LikeEvent serializer");
        return template;
    }

    @Bean
    public ChannelTopic likeTopic() {
        String topicName = redisProperties.getTopic(EventType.LIKED_POST);
        ChannelTopic topic = new ChannelTopic(topicName);
        log.info("Subscribe to Redis topic: {}", topic.getTopic());
        return topic;
    }

    @Bean
    MessageListenerAdapter likeEventListenerAdapter(LikeEventListener likeEventListener){
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(likeEventListener);
        messageListenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(LikeEvent.class));
        messageListenerAdapter.setDefaultListenerMethod("onMessage");
        return messageListenerAdapter;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter likeEventListenerAdapter){
        RedisMessageListenerContainer messageListener = new RedisMessageListenerContainer();
        messageListener.setConnectionFactory(jedisConnectionFactory());
        messageListener.addMessageListener(likeEventListenerAdapter, likeTopic());
        return messageListener;
    }
}
