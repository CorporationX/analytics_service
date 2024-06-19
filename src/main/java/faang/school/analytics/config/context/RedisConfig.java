package faang.school.analytics.config.context;

import faang.school.analytics.listener.CommentEventEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.channel.comment-event-chanel}")
    private String commentEventChannelName;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    MessageListenerAdapter commentEventListener(CommentEventEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean()
    public ChannelTopic commentEventTopic() {
        return new ChannelTopic(commentEventChannelName);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter commentEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(commentEventListener, commentEventTopic());

        return container;
    }
}
