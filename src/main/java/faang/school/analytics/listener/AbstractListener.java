package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    private final List<EventHandler<T>> eventHandlers;

    protected T listenEvent(Message message, Class<T> eventType) throws IOException {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to map message to event type", e);
        }
    }

    protected abstract Class<T> eventType();

    protected abstract void saveEvent(T event);

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            T event = listenEvent(message, eventType());
            log.info(eventHandlers.toString());
            eventHandlers.forEach(handler -> handler.handle(event));
            log.info("Data successfully processed for event {}", event);

            saveEvent(event);

        } catch (IOException e) {
            log.warn("Unsuccessful mapping", e);
            throw new RuntimeException(e);
        }
    }
}
