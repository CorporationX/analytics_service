package faang.school.analytics.config;

import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.subscriber.CommentEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisPubSubConfig {
    @Value("${spring.data.redis.topics.comment_event_topic}")
    private String topic;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Bean
    ChannelTopic commentEventTopic() {
        return new ChannelTopic(topic);
    }

    @Bean
    MessageListenerAdapter messageListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(CommentEventListener commentEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.addMessageListener(messageListener(commentEventListener), commentEventTopic());
        return container;
    }
}
