package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.Event;
import faang.school.analytics.exception.DeserializeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T extends Event> implements MessageListener {

    private final ObjectMapper objectMapper;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
            log.info("Received event from channel: {}", new String(message.getChannel(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Error deserializing JSON to object: ", e);
            throw new DeserializeException("Error deserializing JSON to object: " + e.getMessage());
        }
    }
}