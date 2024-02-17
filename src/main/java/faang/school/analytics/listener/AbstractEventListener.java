package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;

    public T getEvent(Message message, Class<T> clazz) {
        T event;
        try {
            event = objectMapper.readValue(message.getBody(), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Can't deserialize JSON");
        }

        return event;
    }
}
