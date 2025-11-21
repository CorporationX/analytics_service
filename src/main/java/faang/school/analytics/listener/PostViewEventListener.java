package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostViewEventListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.consumers.post-view.topic}",
            containerFactory = "postViewConcurrentKafkaListenerContainerFactory"
    )

    public void consumePostViewEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("Received PostViewEvent from Kafka: {}", record.value());

        try {
            PostViewEvent event = objectMapper.readValue(record.value(), PostViewEvent.class);
            analyticsEventService.processPostViewEvent(event);
            log.info("Successfully processed PostViewEvent for postId: {}", event.postId());

            ack.acknowledge();
            log.info("Acknowledged message for postId: {}", event.postId());
        } catch (Exception e) {
            log.error("Error processing PostViewEvent: {}", e.getMessage());
        }
    }
}
