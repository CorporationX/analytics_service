package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

public abstract class AbstractEventListener<T> implements MessageListener {
    @Autowired
    private ObjectMapper objectMapper;
    private final Class<T> type;

    public AbstractEventListener(Class<T> type) {
        this.type = type;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event = deserialize(message.getBody());
        saveEvent(event);
    }

    private T deserialize(byte[] body) {
        try {
            return objectMapper.readValue(body, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void saveEvent(T event);
}
