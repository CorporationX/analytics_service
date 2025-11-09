package faang.school.analytics.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalCompletedEventConsumer {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "goal-completed", groupId = "goal-completed-group",
            containerFactory = "objectContainerFactory")
    public void consumeEvent(ConsumerRecord<String, Object> kafkaObject) {
        GoalCompletedEvent event = objectMapper.convertValue(kafkaObject.value(), GoalCompletedEvent.class);
        log.info("Получен новый GoalCompletedEvent с goalId: {}", event.goalId());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setAuthorId(event.userId());
        analyticsEvent.setReceiverId(event.goalId());
        analyticsEvent.setEventType(EventType.GOAL_COMPLETED);
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("GoalCompletedEvent c goalId: {} сохранен в бд.", event.goalId());
    }
}
