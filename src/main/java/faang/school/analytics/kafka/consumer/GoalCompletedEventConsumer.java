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

    @KafkaListener(topics = "${spring.data.kafka.topics.goal_completed.name}",
            groupId = "${spring.data.kafka.topics.goal_completed.group_id}",
            containerFactory = "objectContainerFactory")
    public void consumeEvent(ConsumerRecord<String, Object> kafkaObject) {
        try {
            GoalCompletedEvent event = objectMapper.convertValue(kafkaObject.value(), GoalCompletedEvent.class);
            log.info("Получен новый GoalCompletedEvent с goalId: {}", event.goalId());
            AnalyticsEvent analyticsEvent = createAnalyticsEvent(event);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("GoalCompletedEvent c goalId: {} сохранен в бд.", event.goalId());
        } catch (Exception e) {
            log.error("При получении и обработке нового GoalCompletedEvent из Kafka произошла ошибка: {}",
                    e.toString());
        }
    }

    private AnalyticsEvent createAnalyticsEvent(GoalCompletedEvent goalCompletedEvent) {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setAuthorId(goalCompletedEvent.userId());
        analyticsEvent.setReceiverId(goalCompletedEvent.goalId());
        analyticsEvent.setEventType(EventType.GOAL_COMPLETED);
        return analyticsEvent;
    }
}
