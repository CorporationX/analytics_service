package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;

    protected T mapEvent(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            log.error("Error reading from message body: {}, Event type: {}", e.getMessage(), eventType.getName());
            throw new RuntimeException(e);
        }
    }
}
