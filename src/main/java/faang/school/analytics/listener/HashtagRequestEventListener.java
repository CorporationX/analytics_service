package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.HashtagRequestEvent;
import faang.school.analytics.exception.JsonDeserializationException;
import faang.school.analytics.mapper.HashtagRequestEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HashtagRequestEventListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsService;
    private final HashtagRequestEventMapper mapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.hashtag-analytics.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void receive(String message) {
        try {
            log.debug("Received new event id: {}", message);
            HashtagRequestEvent event = objectMapper.readValue(message, HashtagRequestEvent.class);
            analyticsService.saveEvent(mapper.toDto(event));
        } catch (JsonProcessingException e) {
            throw new JsonDeserializationException("Deserialization json %s to event object error", message);
        }
    }
}
