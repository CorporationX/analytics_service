package faang.school.analytics.config.redis;

import faang.school.analytics.listener.ProjectViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties properties;

    @Bean
    public MessageListenerAdapter messageListener(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    public ChannelTopic channelTopic() {
        String channelTopic = properties.getChannels().get("project-profile-attendance");
        return new ChannelTopic(channelTopic);
    }

    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messageListenerAdapter,
            ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, channelTopic);
        return container;
    }
}
