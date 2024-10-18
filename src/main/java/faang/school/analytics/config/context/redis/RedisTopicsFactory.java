package faang.school.analytics.config.context.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;

@Configuration
public class RedisTopicsFactory {
    @Value("${spring.data.redis.channel.post-view}")
    private String postViewEventTopic;

    @Bean
    public Topic postViewEventTopic() {
        return new ChannelTopic(postViewEventTopic);
    }
}
