package faang.school.analytics.listener;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
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

    @KafkaListener(topics = "${app.kafka.topics.subscription-create-events}")
    public void handleSubscriptionCreateEvents(EventDto eventDto, Acknowledgment ack) {
        try {
            analyticsEventService.saveEvent(eventDto);
            ack.acknowledge();
            log.info("Subscription create event saved: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process subscription create event: {}", eventDto, e);
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.comment-create-events}")
    public void handleCommentCreateEvents(CommentEventDto eventDto, Acknowledgment ack) {
        try {
            analyticsEventService.saveEvent(eventDto);
            ack.acknowledge();
            log.info("Comment create event saved: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process comment create event: {}", eventDto, e);
        }
    }
}