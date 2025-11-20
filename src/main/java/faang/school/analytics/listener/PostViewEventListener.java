package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public void consumePostViewEvent(ConsumerRecord<String, String> record) {
        log.info("Received PostViewEvent from Kafka: {}", record.value());

        try {
            PostViewEvent event = objectMapper.readValue(record.value(), PostViewEvent.class);
            analyticsEventService.processPostViewEvent(event);
            log.info("Successfully processed PostViewEvent for postId: {}", event.postId());
        } catch (Exception e) {
            log.error("Error processing PostViewEvent: {}", e.getMessage());
        }
    }
}
