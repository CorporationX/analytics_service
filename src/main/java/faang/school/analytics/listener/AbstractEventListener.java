package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.DeserializeException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.function.Consumer;

@NoArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    @Autowired
    protected ObjectMapper objectMapper;

    public AbstractEventListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new DeserializeException("Error deserializing JSON to object: " + e.getMessage());
        }
    }
}