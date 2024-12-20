package faang.school.analytics.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisConfigProperties(String host, int port, Channel channel) {
    public record Channel(String profileView, String goalCompleted, String adBought, String mentorshipRequest) {
    }
}
