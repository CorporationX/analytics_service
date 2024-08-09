package faang.school.analytics.config.context.redisConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties("spring.data.redis")
public class RedisProperties {

    private int port;
    private String host;
    private Channel channel;

    @Data
    static class Channel {
        private String profileView;
        private String followerView;
    }
}
