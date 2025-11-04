package faang.school.analytics.config.redis;


import faang.school.analytics.listener.PostViewEventListener;
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
public class PostViewEventConfig {

    @Value("${spring.data.redis.channels.post-view-event-channel.name}")
    private String postViewTopic;

    private final PostViewEventListener postViewEventListener;

    @Bean
    RedisMessageListenerContainer postVieEventListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(postViewEventListener);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic(postViewTopic));
        return container;
    }


}
