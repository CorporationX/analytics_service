package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class AbstractEventListener<T>
        implements MessageListener, RedisContainerMessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    public T mapMessage(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            log.error("Failed to deserialize follower event", e);
            throw new RuntimeException(e);
        }
    }

    public void save(AnalyticsEvent analyticsEvent) {
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
