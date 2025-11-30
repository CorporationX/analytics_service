package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.ParsingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    private final Class<T> eventType;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, eventType, this::eventConsumer);
    }

    private void handleEvent(Message message, Class<T> eventType, Consumer<T> eventConsumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            eventConsumer.accept(event);
            log.info("Event {} handled", event);
        } catch (IOException e) {
            log.error("Error while parsing message", e);
            throw new ParsingException(e.getMessage(), e);
        }
    }

    protected abstract void eventConsumer(T event);
}
