package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    protected final AnalyticsEventService analyticsEventService;
    protected final AnalyticsEventMapper analyticsEventMapper;

    @Getter
    private final String channel;

    protected void handleEvent(Message message, Class<T> typeClass, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), typeClass);
            consumer.accept(event);
        } catch (IOException ex) {
            throw new RuntimeException("Error deserializing JSON to object", ex);
        }
    }
}
