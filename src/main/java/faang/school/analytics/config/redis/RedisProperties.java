package faang.school.analytics.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    private String host;
    private int port;
    private Channels channels;

    @Getter
    @Setter
    protected static class Channels {
        private String profile_view;
        private String like_events;
    }
}
