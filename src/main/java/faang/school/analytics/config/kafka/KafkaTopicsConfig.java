package faang.school.analytics.config.kafka;

import faang.school.analytics.config.properties.HashtagAnalyticsTopicProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {

    private final HashtagAnalyticsTopicProperties hashtagAnalyticsTopic;

    @Bean
    public NewTopic hashtagAnalyticsTopic() {
        return createTopic(hashtagAnalyticsTopic.name(),
                hashtagAnalyticsTopic.partitions(),
                hashtagAnalyticsTopic.replicas());
    }

    private NewTopic createTopic(String name, int partitions, int replicas) {
        return TopicBuilder.name(name)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
