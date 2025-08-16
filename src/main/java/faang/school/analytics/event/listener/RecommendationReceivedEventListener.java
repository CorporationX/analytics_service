package faang.school.analytics.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.RecommendationReceivedEventDto;
import faang.school.analytics.exception.event.EventDeserializationException;
import faang.school.analytics.mapper.RecommendationReceivedEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationReceivedEventListener {

    private final ObjectMapper objectMapper;
    private final RecommendationReceivedEventMapper recommendationReceivedEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "${spring.kafka.topics.recommendationReceived}")
    public void listen(String eventText) {
        log.info("Received new event: {}", eventText);
        RecommendationReceivedEventDto recommendationReceivedEventDto = null;

        try {
            recommendationReceivedEventDto = objectMapper.readValue(eventText, RecommendationReceivedEventDto.class);
        } catch (Exception e) {
            log.error("Error parsing event to DTO", e);
            throw new EventDeserializationException("Failed to parse event", e);
        }

        analyticsEventService.saveEvent(
                recommendationReceivedEventMapper.toAnalyticsEventDto(recommendationReceivedEventDto)
        );
    }
}
