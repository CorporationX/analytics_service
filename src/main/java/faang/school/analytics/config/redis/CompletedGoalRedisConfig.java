package faang.school.analytics.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class CompletedGoalRedisConfig {

    @Value("${spring.data.redis.channel.goal-complete.name}")
    private String topicName;

    @Bean
    ChannelTopic completedGoalTopic() {
        return new ChannelTopic(topicName);
    }
}
