package faang.school.analytics.kafka.listener;

import faang.school.analytics.dto.goal.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.utils.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalCompletedEventListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final EventMapper<GoalCompletedEvent> eventMapper;

    @KafkaListener(topics = "${kafka.goal.completed.topic}", groupId = "${spring.kafka.group.id}")
    public void listen(String message) {
        GoalCompletedEvent event = eventMapper.mapMessageToEvent(message, GoalCompletedEvent.class);
        log.info("Received GoalCompletedEvent: {}", event);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEntityFromGoalCompletedEvent(event);
        analyticsEventService.saveAnalyticsEvent(analyticsEvent);
        log.info("Analytics event saved for GoalCompletedEvent: {}", event);
    }
}