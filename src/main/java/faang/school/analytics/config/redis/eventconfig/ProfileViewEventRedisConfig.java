package faang.school.analytics.config.redis.eventconfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class ProfileViewEventRedisConfig extends AbstractEventRedisConfig {

    public ProfileViewEventRedisConfig(
        @Value("${spring.data.redis.channels.profile-view-channel.name}") String topicName,
        @Qualifier("profileViewEventListener") MessageListener eventListener
    ) {
        super(topicName, eventListener);
    }

    @Override
    @Bean("profileViewChannel")
    public ChannelTopic getTopic() {
        return this.topic;
    }

    @Override
    @Bean("profileViewAdapter")
    public MessageListener getAdapter() {
        return this.adapter;
    }
}
