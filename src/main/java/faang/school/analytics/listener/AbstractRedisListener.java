package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEventService;
import faang.school.analytics.model.entity.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public abstract class AbstractRedisListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    protected void handleEvent(Class<T> type, Message message, Function<T, AnalyticsEvent> function) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            AnalyticsEvent analyticsEvent = function.apply(event);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}