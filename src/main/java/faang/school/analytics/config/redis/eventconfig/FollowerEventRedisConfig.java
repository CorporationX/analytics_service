package faang.school.analytics.config.redis.eventconfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class FollowerEventRedisConfig extends AbstractEventRedisConfig {

    public FollowerEventRedisConfig(
        @Value("${spring.data.redis.channels.follower-channel.name}") String topicName,
        @Qualifier("followerEventListener") MessageListener eventListener
    ) {
        super(topicName, eventListener);
    }
    @Override
    @Bean(value = "followerEventTopic")
    public ChannelTopic getTopic() {
        return this.topic;
    }

    @Override
    @Bean(value = "followerAdapter")
    public MessageListener getAdapter() {
        return this.adapter;
    }
}
