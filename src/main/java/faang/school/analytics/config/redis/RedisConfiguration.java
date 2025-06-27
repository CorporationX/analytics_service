package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listner.event.FollowerEventListner;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    private final FollowerEventListner followerEventListner;
    private final RedisProperties redisProperties;
    private final Map<String, ChannelTopic> topics = new HashMap<>();

    @PostConstruct
    public void initTopics() {
        redisProperties.getChannels().forEach((key, value) -> {
            topics.put(key, new ChannelTopic(value));
        });
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        String channel = redisProperties.getChannels().get("follower_event");
        if (channel == null) {
            String err = "Redis channel 'follower_event' not configured";
            log.error(err);
            throw new IllegalStateException(err);
        }
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(followerEventListner, new ChannelTopic(channel));

        log.info("Configured Redis listener for channel: {}", channel);

        return container;
    }
}