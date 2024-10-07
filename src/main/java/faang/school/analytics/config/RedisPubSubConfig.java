package faang.school.analytics.config;

import faang.school.analytics.subscriber.CommentEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPubSubConfig {
    @Value("${spring.data.redis.topics.comment_event_topic}")
    private String topic;
    @Bean
    ChannelTopic commentEventTopic() {
        return new ChannelTopic(topic);
    }
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }
    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new CommentEventListener());
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), commentEventTopic());
        return container;
    }
}
