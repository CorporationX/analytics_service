package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public abstract class RedisMessageListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), getEventType());
            saveEvent(event);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse message", e);
        }
    }

    protected abstract Class<T> getEventType();

    public abstract void saveEvent(T event);
}
