package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.annotation.Nullable;
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
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(@Nullable Message message, byte[] pattern) {
        if (message == null) {
            log.error("Received message is null in GoalCompletedEventListener");
            return;
        }
        try {
            GoalCompletedEvent goalCompletedEvent = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            AnalyticsEvent event = analyticsEventMapper.goalCompletedEventToEntity(goalCompletedEvent);
            analyticsEventService.saveEvent(event);
        } catch (IOException e) {
            log.error("Failed to parse or read message body", e);
            throw new MessageProcessingException("Message parsing failed", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing the message", e);
            throw new MessageProcessingException("Unexpected failure", e);
        }
    }
}
