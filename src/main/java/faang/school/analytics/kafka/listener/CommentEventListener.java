package faang.school.analytics.kafka.listener;

import faang.school.analytics.dto.event.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.EventService;
import faang.school.analytics.utils.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener {

    private final EventService eventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final EventMapper<CommentEvent> eventEventMapper;

    @KafkaListener(topics = "${kafka.comment.topic}", groupId = "${spring.kafka.group.id}")
    public void listen(String message) {
        CommentEvent event = eventEventMapper.mapMessageToEvent(message, CommentEvent.class);
        log.info("Received GoalCompletedEvent: {}", event);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEventFromCommentEvent(event);
        analyticsEvent.setEventType(EventType.COMMENT_EVIL);
        eventService.saveAnalyticsEvent(analyticsEvent);
        log.info("Analytics event saved for GoalCompletedEvent: {}", event);
    }
}