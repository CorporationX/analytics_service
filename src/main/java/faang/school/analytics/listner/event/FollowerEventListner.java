package faang.school.analytics.listner.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowerEventListner implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent followerEvent = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            log.info("Processing event: {}", followerEvent);
            analyticsEventService.saveFollowerEvent(followerEvent);
            log.info("Saving Event: {}", followerEvent);
        } catch (JsonProcessingException e) {
            log.error("Failed tom parse FollowerEvent JSON: {}", message.getBody(), e);
        } catch (IOException e) {
            log.error("Failed to deserialize message", e);
        }
    }
}