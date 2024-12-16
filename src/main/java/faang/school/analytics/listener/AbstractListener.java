package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    private final List<EventHandler<T>> eventHandlers;

    protected T listenEvent(Message message, Class<T> eventType) throws IOException {
        if (message.getBody().length == 0) {
            log.error("Message body is empty {}", message.getBody());
            throw new IOException("Message body is empty");
        }

        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            log.error("Failed to map message to event type", e);
            throw new EventDeserializationException("Failed to map message to event type");
        }
    }

    protected Class<T> eventType() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    protected abstract void handleEvent(T event);

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            T event = listenEvent(message, eventType());

            if (eventHandlers != null && !eventHandlers.isEmpty()) {
                log.info("Processing event with handlers: {}", eventHandlers);
                eventHandlers.forEach(handler -> handler.handle(event));
            } else {
                log.warn("No event handlers available for event: {}", event);
            }

            log.info("Data successfully processed for event {}", event);
            handleEvent(event);

        } catch (IOException e) {
            log.error("Failed to process event", e);
            throw new EventProcessingException("Failed to process event", e);
        }
    }
}
