package faang.school.analytics.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received Like event: {}", message.toString());
        try {
            LikeEvent likeEvent = objectMapper.readValue(message.getBody(), LikeEvent.class);
            analyticsEventService.saveLikeEvent(likeEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
