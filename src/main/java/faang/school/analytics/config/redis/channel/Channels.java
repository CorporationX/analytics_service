package faang.school.analytics.config.redis.channel;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.channels")
public class Channels {
    private String channelProfileView;
    private String channelFollower;
    private String channelComment;
    private String channelLikeEvent;
}