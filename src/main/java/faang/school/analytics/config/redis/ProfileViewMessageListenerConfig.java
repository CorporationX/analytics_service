package faang.school.analytics.config.redis;

import faang.school.analytics.listener.ProfileViewCreateEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class ProfileViewMessageListenerConfig {
    @Bean
    MessageListenerAdapter profileViewListener(
            ProfileViewCreateEventListener profileViewCreateEventListener
    ) {
        return new MessageListenerAdapter(profileViewCreateEventListener);
    }

    @Bean
    ChannelTopic profileTopic(@Value("${spring.data.redis.channels.profile_view}") String topic) {
        return new ChannelTopic(topic);
    }
}
