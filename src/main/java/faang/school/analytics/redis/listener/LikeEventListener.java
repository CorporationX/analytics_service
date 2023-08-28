package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.redis.event.LikeEvent;
import faang.school.analytics.service.LikeEventService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class LikeEventListener implements MessageListener {

    private final LikeEventService likeEventService;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            objectMapper.readValue(message.getBody(), LikeEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Message received: {}", message.toString());
        try {
            likeEventService.saveMessage(message.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}