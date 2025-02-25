package faang.school.analytics.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisConfigProperties(
        int port,
        String host,
        Channel channel) {
    public record Channel(
            String channelFollower,
            String profileView
    ) {
    }
}
