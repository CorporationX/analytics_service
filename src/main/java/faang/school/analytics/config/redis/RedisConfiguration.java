package faang.school.analytics.config.redis;

import faang.school.analytics.listener.ProfileViewEventListener;
import faang.school.analytics.listener.SearchAppearanceEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    private final RedisConnectionFactory connectionFactory;
    private final SearchAppearanceEventListener searchAppearanceEventListener;
    private final ProfileViewEventListener profileViewEventListener;
    private List<String> topics;

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter searchAppearanceEventListenerAdapter = new MessageListenerAdapter(searchAppearanceEventListener);
        MessageListenerAdapter profileViewEventListenerAdapter = new MessageListenerAdapter(profileViewEventListener);
        container.addMessageListener(searchAppearanceEventListenerAdapter, new ChannelTopic("search-appearance"));
        container.addMessageListener(profileViewEventListenerAdapter, new ChannelTopic("profile-view"));
        return container;
    }
}
