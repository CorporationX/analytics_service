package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;

@Slf4j
@AllArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    @Getter
    private final Topic topic;

    public abstract void saveEvent(T event);

    public abstract Class<T> getEventType();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        try {
            T event = objectMapper.readValue(messageBody, getEventType());
            saveEvent(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert message to event", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save event", e);
        }
    }
}