package faang.school.analytics.config.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RedisProperties {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("spring.data.redis.topic.buy-premium-channel")
    private String buyPremiumTopic;

    @Value("${spring.data.redis.topic.recommendation-event}")
    private String recommendationEventTopic;
}
