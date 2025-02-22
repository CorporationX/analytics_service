package faang.school.analytics.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.EventService;
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
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.comment.topic}", groupId = "${spring.kafka.group.id}")
    public void listen(String input) {
        CommentEvent event = mapInputToGoalCompletedEvent(input);
        log.info("Received GoalCompletedEvent: {}", event);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEventFromCommentEvent(event);
        analyticsEvent.setEventType(EventType.COMMENT_EVIL);
        eventService.saveAnalyticsEvent(analyticsEvent);
        log.info("Analytics event saved for GoalCompletedEvent: {}", event);
    }

    private CommentEvent mapInputToGoalCompletedEvent(String input) {
        CommentEvent event;
        try {
            event = objectMapper.readValue(input, CommentEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return event;
    }
}