package faang.school.analytics.config;

import faang.school.analytics.listener.GoalCompletedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final GoalCompletedEventListener goalCompletedEventListener;

    @Value("${spring.data.redis.channels.goal-completed}")
    private String goalCompletedTopic;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(goalCompletedListenerAdapter(), goalCompletedTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter goalCompletedListenerAdapter() {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedTopic);
    }
}

