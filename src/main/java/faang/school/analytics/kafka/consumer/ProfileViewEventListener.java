package faang.school.analytics.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.FailedDeserealizationException;
import faang.school.analytics.kafka.events.ProfileViewEvent;
import faang.school.analytics.mapper.ProfileViewEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileViewEventListener {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final ProfileViewEventMapper profileViewEventMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.profile-view-event-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenProfileViewEvent(String message) {
        log.info("Received raw string message: {}", message);
        try {
            ProfileViewEvent event = objectMapper.readValue(message, ProfileViewEvent.class);
            log.info("Successfully deserialized ProfileViewEvent: {}", event);
            analyticsEventRepository.save(profileViewEventMapper.toAnalyticsEvent(event));
            log.info("Successfully saved mapped ProfileViewEvent to the database: {}",
                    profileViewEventMapper.toAnalyticsEvent(event));
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize ProfileViewEvent from message: {}", message, e);
            throw new FailedDeserealizationException("Failed to deserialize ProfileViewEvent from message: " + message + e);
        }
    }
}
