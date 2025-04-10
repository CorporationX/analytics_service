package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.exception.AnalyticsConvertingException;
import faang.school.analytics.mapper.GoalCompletedEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoalCompletedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsService;
    private final GoalCompletedEventMapper eventMapper;

    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        try {
            log.debug("Received new goal completed event: {}", message.getBody());
            GoalCompletedEvent event = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            analyticsService.saveEvent(eventMapper.toAnalyticsDto(event));
        } catch (IOException e) {
            throw new AnalyticsConvertingException("Failed to deserialize message", e);
        }
    }
}
