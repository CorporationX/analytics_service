package faang.school.analytics.redis.config;

import faang.school.analytics.redis.lisener.FollowerEventListener;
import faang.school.analytics.redis.lisener.ProfileViewEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public class FollowerConfig {

    private final FollowerEventListener followerEventListener;

    @Autowired
    public FollowerConfig(FollowerEventListener followerEventListener) {
        this.followerEventListener = followerEventListener;
    }

    @Bean
    public RedisMessageListenerContainer followerEventListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(followerEventListener);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic("follower_channel"));

        return container;
    }
}
