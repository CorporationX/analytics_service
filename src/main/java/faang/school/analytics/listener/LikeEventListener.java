package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
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
public class LikeEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("LikeEventListener received a message:\n{}", message.getBody());
        try {
            LikeEvent event = objectMapper.readValue(message.getBody(), LikeEvent.class);
            log.info("Message was successfully received and converted into LikeEvent:\n{}", event);
            analyticsEventService.addLikeEvent(event);
        } catch (IOException e) {
            log.warn("Wasn't able to convert message into LikeEvent. Incoming message:\n{}", message.getBody());
            throw new RuntimeException(e);
        }
    }
}
