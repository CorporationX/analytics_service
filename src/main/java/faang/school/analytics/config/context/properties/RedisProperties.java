package faang.school.analytics.config.context.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mentorship")
public class RedisProperties {
    private String host;
    private int port;
    private String channel;
}
