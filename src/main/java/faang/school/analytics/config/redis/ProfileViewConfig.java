package faang.school.analytics.config.redis;

import faang.school.analytics.redis.lisener.ProfileViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@RequiredArgsConstructor
@Configuration
public class ProfileViewConfig {

    private final ProfileViewEventListener profileViewEventListener;

    @Bean
    public RedisMessageListenerContainer profileViewEventListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(profileViewEventListener);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic("profile_view_channel"));

        return container;
    }
}
