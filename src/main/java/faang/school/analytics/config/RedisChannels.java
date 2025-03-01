package faang.school.analytics.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.redis.channel")
@Getter
@Setter
public class RedisChannels {
    private String recommendationChannel;
}
