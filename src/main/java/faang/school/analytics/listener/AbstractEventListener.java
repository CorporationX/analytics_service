package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.channel.ChannelInfo;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener, ChannelInfo {
    private final ObjectMapper objectMapper;
    protected final AnalyticsEventService analyticsEventService;
    protected final AnalyticsEventMapper analyticsEventMapper;

    protected void handleEvent(Message message, Class<T> typeClass, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), typeClass);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing JSON to object", e);
        }
    }
}
