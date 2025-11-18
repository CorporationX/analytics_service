package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.LikeEvent;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikeEventListener implements MessageListener {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEvent likeEvent = objectMapper.readValue(message.getBody(), LikeEvent.class);
            log.info("Received LikeEvent: {}", likeEvent);
        } catch (IOException e) {
            log.error("Failed to parse LikeEvent", e);
            throw new RuntimeException(e);
        }
    }
}
