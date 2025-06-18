package faang.school.analytics.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.mapper.FollowEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener {
    private final ObjectMapper objectMapper;
    private final FollowEventMapper mapper;
    private final AnalyticsEventService service;
    private final AnalyticsEventRepository repository;

    @KafkaListener(
            topics = "${spring.kafka.topics.follower-events.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(String message) {
        log.info("Received raw JSON: {}", message);
        try {
            FollowerEvent event = objectMapper.readValue(message, FollowerEvent.class);
            log.info("Deserialized FollowerEvent: {}", event);
            AnalyticsEvent entity = mapper.toEntity(event);
            log.info("Mapped to AnalyticsEvent: {}", entity);
            repository.save(entity);
            log.info("Saved AnalyticsEvent id={}", entity.getId());
        } catch (JsonProcessingException e) {
            log.error("Failed to parse FollowerEvent JSON", e);
        }
    }
}
