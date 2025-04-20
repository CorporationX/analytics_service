package faang.school.analytics.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class HashtagAnalyticsTopicProperties {

    @Value("${spring.kafka.topics.hashtag-analytics.name}")
    private String name;

    @Value("${spring.kafka.topics.hashtag-analytics.partitions}")
    private int partitions;

    @Value("${spring.kafka.topics.hashtag-analytics.replicas}")
    private int replicas;
}
