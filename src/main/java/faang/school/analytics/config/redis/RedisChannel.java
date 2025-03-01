package faang.school.analytics.config.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.redis.channel")
public class RedisChannel {
    private String profileView;
    private String projectView;
    private String follower;
    private String postPublished;
}
