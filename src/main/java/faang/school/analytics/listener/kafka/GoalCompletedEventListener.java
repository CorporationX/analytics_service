package faang.school.analytics.listener.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.kafka.KafkaProperties;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.mapper.event.UserServiceEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalCompletedEventListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService service;
    private final UserServiceEventMapper userServiceEventMapper;
    private final KafkaProperties properties;

    @KafkaListener(topics = "${spring.data.kafka.topics.goal-completed}")
    public void listen(String json) {
        if (!properties.isUseKafka()) return;
        try {
            GoalCompletedEvent event = objectMapper.readValue(json, GoalCompletedEvent.class);
            service.saveEvent(userServiceEventMapper.goalCompleteToAnalytics(event));
            log.info("Goal {} completion was saved, goalId: {}", event.goalTitle(), event.goalId());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }
}
