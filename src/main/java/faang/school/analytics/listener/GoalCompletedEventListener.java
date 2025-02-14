package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
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

    @KafkaListener(topics = "${kafka.topic.goal-completed}", groupId = "analytics-group")
    public void listen(String input) {
        GoalCompletedEvent event = null;
        try {
            event = objectMapper.readValue(input, GoalCompletedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Received GoalCompletedEvent: {}", event);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEntity(event);
        analyticsEventService.saveAnalyticsEvent(analyticsEvent);
        log.info("Analytics event saved for GoalCompletedEvent: {}", event);
    }
}