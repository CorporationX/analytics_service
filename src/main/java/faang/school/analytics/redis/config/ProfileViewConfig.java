package faang.school.analytics.redis.config;

import faang.school.analytics.redis.lisener.ProfileViewEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public class ProfileViewConfig {

    private final ProfileViewEventListener profileViewEventListener;

    @Autowired
    public ProfileViewConfig(ProfileViewEventListener profileViewEventListener) {
        this.profileViewEventListener = profileViewEventListener;
    }

    @Bean
    public RedisMessageListenerContainer profileViewEventListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(profileViewEventListener);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic("profile_view_channel"));

        return container;
    }
}
