package faang.school.analytics.config.context.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    private Channels channels;

    @Getter
    @Setter
    protected static class Channels {
        private Channel postViewChannel;

        @Getter
        @Setter
        protected static class Channel {
            String name;
        }

    }
}
