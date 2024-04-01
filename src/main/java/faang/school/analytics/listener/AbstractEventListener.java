package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.handler.EventHandler;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final List<EventHandler<T>> eventHandlers;
    protected final AnalyticsEventService analyticsEventService;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        T event = mapToEvent(message, type);
        consumer.accept(event);
        log.info("Success deserializing JSON to object");
    }

    protected void processEventForAllHandlers(Message message, Class<T> type) {
        T event = mapToEvent(message, type);
        eventHandlers.forEach(handler -> handler.handle(event));
    }

    private T mapToEvent(Message message, Class<T> type) {
        try {
            return objectMapper.readValue(message.getBody(), type);
        } catch (IOException e) {
            log.error("Error deserializing JSON to object", e);
            throw new RuntimeException("Error deserializing JSON to object");
        }
    }

}
