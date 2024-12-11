package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestedEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
   private final ObjectMapper objectMapper;

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2.0)
    )
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipRequestEvent mentorshipRequestEvent = objectMapper.readValue(message.getBody(),MentorshipRequestEvent.class);
            analyticsEventService.saveMentorshipRequestEvent(mentorshipRequestEvent);
            log.info("MentorshipRequestEvent save");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error while processing mentorship request event.", ex);
        }
    }
}
