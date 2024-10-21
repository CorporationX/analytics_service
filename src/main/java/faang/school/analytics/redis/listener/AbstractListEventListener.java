package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;

import java.util.List;

@Slf4j
@AllArgsConstructor
public abstract class AbstractListEventListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    @Getter
    private final Topic topic;

    public abstract void saveEvents(List<T> events);

    public abstract Class<T> getEventType();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        try {
            List<T> events = objectMapper.readValue(messageBody,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, getEventType()));
            saveEvents(events);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON message: {}", messageBody, e);
        } catch (Exception e) {
            log.error("Unexpected error processing message: {}", messageBody, e);
        }
    }
}