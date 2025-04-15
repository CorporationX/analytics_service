package faang.school.analytics.config.context.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mentorship")
public record RedisProperties(String host, int port, String channel) {
}
