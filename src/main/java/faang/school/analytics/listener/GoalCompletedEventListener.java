package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.GoalCompletedMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoalCompletedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final GoalCompletedMapper goalCompletedMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEvent goalCompletedEvent = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            AnalyticsEvent event = goalCompletedMapper.toEntity(goalCompletedEvent);
            event.setEventType(EventType.GOAL_COMPLETED);
            analyticsEventService.saveEvent(event);
        } catch (IOException e) {
            log.error("Exception occurred while parsing the message", e);
            throw new MessageProcessingException("Failed to process message: " + message, e);
        }
    }
}
