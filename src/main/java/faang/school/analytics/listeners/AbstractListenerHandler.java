package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractListenerHandler<T> implements MessageListener {

    private final ObjectMapper objectMapper;

    public void handler(Message message, Class<T> type, Consumer<T> consumer) {
        T event = objectMapper.convertValue(message.getBody(), type);
        consumer.accept(event);
        log.info("Received event from: {}", new String(message.getChannel(), StandardCharsets.UTF_8));
    }
}