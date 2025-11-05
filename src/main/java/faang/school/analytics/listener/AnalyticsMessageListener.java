package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsMessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @KafkaListener(topics = "analytics")
    public void handleMessage(String message) {
        try {
            EventDto eventDto = objectMapper.readValue(message, EventDto.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(eventDto);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("Analytics event saved: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process analytics event from channel: {}", e);
        }
    }
}