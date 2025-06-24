package faang.school.analytics.listener;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PostLikeEventListener extends EventListener {

    @Value("${kafka.topic-name}")
    private String topicName;

    @Value("${kafka.group}")
    private String groupName;

    public PostLikeEventListener(AnalyticsEventService analyticsEventService,
                                 AnalyticsEventMapper analyticsEventMapper) {
        super(analyticsEventService, analyticsEventMapper);
    }

    @KafkaListener(topics = "${kafka.topic-name}", groupId = "${kafka.group}")
    public void consume(ConsumerRecord<String, Object> event) {
        saveEvent(event, EventType.POST_LIKE);
    }
}
