package faang.school.analytics.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaProperty property;

    @Bean
    public NewTopic analiticsNewTopic() {
        return TopicBuilder.name(property.topic().analiticsEventNew())
                .build();
    }
}
