package faang.school.analytics.config.redis;

import faang.school.analytics.redis.lisener.FollowerEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@RequiredArgsConstructor
@Configuration
public class FollowerConfig {

    private final FollowerEventListener followerEventListener;

    @Bean
    public RedisMessageListenerContainer followerEventListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(followerEventListener);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic("follower_channel"));

        return container;
    }
}
