package faang.school.analytics.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.goal.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoalCompletedEventListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.goal.completed.topic}", groupId = "${spring.kafka.group.id}")
    public void listen(String input) {
        GoalCompletedEvent event = mapInputToGoalCompletedEvent(input);
        log.info("Received GoalCompletedEvent: {}", event);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEntityFromGoalCompletedEvent(event);
        analyticsEventService.saveAnalyticsEvent(analyticsEvent);
        log.info("Analytics event saved for GoalCompletedEvent: {}", event);
    }

    private GoalCompletedEvent mapInputToGoalCompletedEvent(String input) {
        GoalCompletedEvent event;
        try {
            event = objectMapper.readValue(input, GoalCompletedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return event;
    }
}