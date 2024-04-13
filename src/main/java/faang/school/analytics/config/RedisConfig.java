package faang.school.analytics.config;

import faang.school.analytics.subscriber.ProfileViewEventListener;
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
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.profile-view-channel.name}")
    private String profileViewChannel;
    private final ProfileViewEventListener profileViewEventListener;


    @Bean
    public ChannelTopic profileViewTopic(){
        return new ChannelTopic(profileViewChannel);
    }

    @Bean
    public MessageListenerAdapter profileViewEventListenerAdapter(){
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(profileViewEventListenerAdapter(), profileViewTopic());
        return container;
    }
}
