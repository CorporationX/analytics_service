package faang.school.analytics.listener;

import faang.school.analytics.model.EventType;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostLikeEventListener {

    @Value("${kafka.topic-name}")
    private String topicName;

    @Value("${kafka.group}")
    private String groupName;

    private EventListener eventListener;

    @KafkaListener(topics = "${kafka.topic-name}", groupId = "${kafka.group}")
    public void consume(ConsumerRecord<String, Object> event) {
        eventListener.saveEvent(event, EventType.POST_LIKE);
    }
}
