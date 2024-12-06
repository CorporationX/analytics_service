package faang.school.analytics.config;

import faang.school.analytics.listener.ProfileViewEventListener;
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

    private final ProfileViewEventListener profileViewEventListener;

    @Value("${spring.data.redis.channel.profile-view-channel.name}")
    private String profileViewChannel;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        ChannelTopic profileViewTopic = new ChannelTopic(profileViewChannel);
        MessageListenerAdapter profileViewMessageListenerAdapter = new MessageListenerAdapter(profileViewEventListener);
        container.addMessageListener(profileViewMessageListenerAdapter, profileViewTopic);

        return container;
    }
}
