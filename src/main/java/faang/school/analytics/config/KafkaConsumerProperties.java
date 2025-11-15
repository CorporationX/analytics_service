package faang.school.analytics.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka.consumers")
public class KafkaConsumerProperties {

    private ConsumerConfig profileView;

    public Map<String, String> getAllTypeMappings() {
        Map<String, String> mappings = new HashMap<>();
        mappings.put(profileView.getTopic(), profileView.getEventClass());
        return mappings;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ConsumerConfig {
        private String topic;
        private String groupId;
        private int concurrency;
        private String eventClass;
    }
}

