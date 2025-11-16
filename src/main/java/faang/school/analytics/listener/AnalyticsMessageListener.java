package faang.school.analytics.listener;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsMessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @KafkaListener(topics = "${app.kafka.topics.subscription-create-events}", properties = "spring.json.value.default.type=faang.school.analytics.dto.EventDto")
    public void handleSubscriptionCreateEvents(EventDto eventDto, Acknowledgment ack) {
        try {
            AnalyticsEvent event = analyticsEventMapper.toEntity(eventDto);
            analyticsEventService.saveEvent(event);
            ack.acknowledge();
            log.info("Subscription create event saved: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process subscription create event: {}", eventDto, e);
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.comment-create-events}", properties = "spring.json.value.default.type=faang.school.analytics.dto.CommentEventDto")
    public void handleCommentCreateEvents(CommentEventDto eventDto, Acknowledgment ack) {
        try {
            AnalyticsEvent event = analyticsEventMapper.toEntity(eventDto);
            analyticsEventService.saveEvent(event);
            ack.acknowledge();
            log.info("Comment create event saved: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process comment create event: {}", eventDto, e);
        }
    }
}