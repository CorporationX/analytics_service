package faang.school.analytics.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.kafka.topics.hashtag-analytics")
public record HashtagAnalyticsTopicProperties(
        String name,
        int partitions,
        int replicas
) {
}
