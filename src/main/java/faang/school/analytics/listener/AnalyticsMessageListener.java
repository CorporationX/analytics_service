package faang.school.analytics.listener;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsMessageListener {

    private final AnalyticsEventService analyticsEventService;

    @Value("${app.kafka.topics.analytics}")
    private String analyticsTopic;

    @KafkaListener(topics = "${app.kafka.topics.analytics}")
    public void handleMessage(EventDto eventDto) {
        try {
            analyticsEventService.saveEvent(eventDto);
            log.info("Analytics event saved: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process analytics event from channel: {}", eventDto, e);
        }
    }
}