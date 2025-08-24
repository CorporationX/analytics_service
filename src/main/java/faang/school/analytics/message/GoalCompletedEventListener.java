package faang.school.analytics.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoalCompletedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper mapper;
    private final AnalyticsEventService service;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEvent event = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            AnalyticsEvent entity = mapper.toAnalyticsEvent(event);
            service.saveEvent(entity);
            log.info("Saved AnalyticsEvent for userId={}, type={}", event.userId(), entity.getEventType());
        } catch (Exception e) {
            log.error("Failed to process GoalCompletedEvent from Redis", e);
        }
    }
}
