package faang.school.analytics.config.redis.eventconfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class ProjectViewEventRedisConfig extends AbstractEventRedisConfig {

    public ProjectViewEventRedisConfig(
        @Value("${spring.data.redis.channels.project-view-channel.name}") String topicName,
        @Qualifier("projectViewEventListener") MessageListener eventListener
    ) {
        super(topicName, eventListener);
    }
    @Override
    @Bean("projectViewChannel")
    public ChannelTopic getTopic() {
        return this.topic;
    }

    @Override
    @Bean("projectViewAdapter")
    public MessageListener getAdapter() {
        return this.adapter;
    }
}
