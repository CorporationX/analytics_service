package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.NewCommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewCommentEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    @Retryable(retryFor = IOException.class,
            maxAttemptsExpression = "@retryProperties.maxAttempts",
            backoff = @Backoff(
                    delayExpression = "@retryProperties.initialDelay",
                    multiplierExpression = "@retryProperties.multiplier",
                    maxDelayExpression = "@retryProperties.maxDelay"
            )
    )
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Received message from Redis: {}", new String(message.getBody(), StandardCharsets.UTF_8));
            NewCommentEvent event = objectMapper.readValue(message.getBody(), NewCommentEvent.class);
            handleEvent(event);
        } catch (IOException e) {
            String errorMessage = new String(message.getBody(), StandardCharsets.UTF_8);
            log.error("Error while deserializing {} from Redis. Error: {}", errorMessage, e.getMessage());
            throw new RuntimeException("Error while deserializing", e);
        }
    }

    private void handleEvent(NewCommentEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.newCommentEventToEntity(event);
        analyticsEventService.saveEvent(analyticsEvent);
    }

}
