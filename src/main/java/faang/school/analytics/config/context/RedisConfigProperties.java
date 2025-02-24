package faang.school.analytics.config.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedisConfigProperties {
    private String host;
    private int port;
    private String channelFollower;
    private String commentChannel;
}
