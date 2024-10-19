package faang.school.analytics.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class RedisTopicsFactory {
    @Value("${app.user-redis-config.profile_view_event_topic}")
    private String profileViewEventTopic;

    @Value("${spring.data.redis.channel.post-view}")
    private String postViewEventTopic;

    @Bean
    public ChannelTopic postViewEventTopic() {
        return new ChannelTopic(postViewEventTopic);
    }

    @Bean
    public ChannelTopic profileViewEventTopic() {
        return new ChannelTopic(profileViewEventTopic);
    }
}
