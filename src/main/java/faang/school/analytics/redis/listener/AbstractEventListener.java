package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T>
        implements MessageListener, RedisListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    public T mapMessage(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            log.error("Failed to deserialize profile view event", e);
            throw new IllegalArgumentException("Failed to deserialize message body", e);
        }
    }

    public void save(AnalyticsEvent analyticsEvent) {
        analyticsEventService.saveEvent(analyticsEvent);
    }
}