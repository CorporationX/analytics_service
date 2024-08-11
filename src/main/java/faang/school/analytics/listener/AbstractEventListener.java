package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    protected void handleEvent(Class<T> clazz, Message message) {
        try {
            T event = objectMapper.readValue(message.getBody(), clazz);
            analyticsEventService.saveEvent(map(event));
        } catch (IOException e) {
            log.error("Failed to deserialize follower event", e);
            throw new RuntimeException(e);
        }
    }

    abstract AnalyticsEvent map(T t);
}
