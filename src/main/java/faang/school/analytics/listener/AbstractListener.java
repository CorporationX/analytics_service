package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    private final List<EventHandler<T>> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event;
        try {
            event = listenEvent(message);
        } catch (IOException e) {
            log.error("mapping was failed", e);
            throw new RuntimeException("mapping was failed");
        }
        List<EventHandler<T>> filteredHandlers = handlers.stream()
                .filter(tEventHandler -> tEventHandler.canHandle(event))
                .toList();
        log.info("filtered handlers: {}", filteredHandlers.toString());
        filteredHandlers.forEach(eventHandler -> eventHandler.handle(event));
    }

    protected abstract T listenEvent(Message message) throws IOException;
}
