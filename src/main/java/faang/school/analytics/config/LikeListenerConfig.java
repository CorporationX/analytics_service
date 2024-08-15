package faang.school.analytics.config;

import faang.school.analytics.event.LikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@RequiredArgsConstructor
@Configuration
public class LikeListenerConfig {
    private final JedisConnectionFactory jedisConnectionFactory;

    @Value("${spring.data.redis.topic.like}")
    private String likeTopic;

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, nameTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic nameTopic() {
        return new ChannelTopic(likeTopic);
    }
}