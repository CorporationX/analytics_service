package faang.school.analytics.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.MessageMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener implements MessageListener {

    private final ObjectMapper objectMapper;

    protected <T> T getEvent(Message message, Class<T> eventClass) {
        try {
            log.info("Trying to convert message to {}", eventClass.getName());
            return objectMapper.readValue(message.getBody(), eventClass);
        } catch (IOException e) {
            MessageMappingException ex = new MessageMappingException(String.format(
                    "Failed to map message to %s", eventClass.getName()));
            log.error("MessageMappingException: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
