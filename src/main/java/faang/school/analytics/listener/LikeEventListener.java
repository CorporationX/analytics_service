package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeEventListener implements RedisChannelEventListeners {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (message == null || message.getBody() == null) {
            log.warn("Received null message or empty body. Pattern: {}", pattern);
            return;
        }
        String rawMessage = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Received raw Redis message: {}", rawMessage);

        try {
            LikeEvent likeEvent = objectMapper.readValue(message.getBody(), LikeEvent.class);
            log.info("Parsed MentorshipRequestedEvent: {}", likeEvent);
            analyticsEventService.saveEvent(analyticsEventMapper.toLikeEntity(likeEvent));
        } catch (IOException e) {
            log.error("Failed to parse LikeEvent", e);
        }
    }

    @Override
    public String getChannel() {
        return "like_channel";
    }
}
