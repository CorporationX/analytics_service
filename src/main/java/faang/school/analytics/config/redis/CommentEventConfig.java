package faang.school.analytics.config.redis;

import faang.school.analytics.redis.lisener.CommentEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@RequiredArgsConstructor
@Configuration
public class CommentEventConfig {

    private final CommentEventListener commentEventListener;

    @Bean
    public RedisMessageListenerContainer commentEventListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(commentEventListener);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic("post_comment_channel"));

        return container;
    }
}
