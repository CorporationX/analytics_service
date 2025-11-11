package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEventDto;
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

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.subscription-events}")
    public void handleSubscriptionEvents(String jsonEvent) {
        try {
            EventDto eventDto = objectMapper.readValue(jsonEvent, EventDto.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(eventDto);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("Subscription event saved: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process subscription event: {}", jsonEvent, e);
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.comment-create-events}")
    public void handleCommentCreateEvents(String jsonEvent) {
        try {
            CommentEventDto eventDto = objectMapper.readValue(jsonEvent, CommentEventDto.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(eventDto);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("Comment create event saved: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process comment create event: {}", jsonEvent, e);
        }
    }
}