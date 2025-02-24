package faang.school.analytics.config.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.channels")
public class Channels {
    private String channelFollower;
    private String commentChannel;
}
