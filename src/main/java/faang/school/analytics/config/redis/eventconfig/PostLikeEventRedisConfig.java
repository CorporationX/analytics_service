package faang.school.analytics.config.redis.eventconfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class PostLikeEventRedisConfig extends AbstractEventRedisConfig {

    public PostLikeEventRedisConfig(
        @Value("${spring.data.redis.channels.like-channel.name}") String topicName,
        @Qualifier("postLikeEventListener") MessageListener eventListener
    ) {
            super(topicName, eventListener);
    }

    @Override
    @Bean("postLikeChannel")
    public ChannelTopic getTopic() {
        return this.topic;
    }

    @Override
    @Bean("postLikeAdapter")
    public MessageListener getAdapter() {
        return this.adapter;
    }
}
