package faang.school.analytics.config.redis;

import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Setter
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    private int port;

    private String host;

    private Channels channels;

    @Data
    public static class Channels {

        private String likesChannel;

        private String commentsChannel;
    }
}
