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

    private Channel channel;

    @Getter
    @Setter
    protected static class Channel {
        private String profileView;
        private String projectViewChannel;
        private String likeEvents;
        private String mentorshipRequestTopic;
        private String postViewEvent;
    }
}
