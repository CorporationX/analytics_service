package faang.school.analytics.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.service.CommentEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener implements MessageListener {
    private final CommentEventService service;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent commentEvent;
        try {
            commentEvent = objectMapper.readValue(message.getBody(), CommentEvent.class);
        } catch (IOException e) {
            log.error("message did not read", e);
            throw new RuntimeException(e);
        }
        log.info("the event was received: " + commentEvent);
        service.save(commentEvent);
    }
}
