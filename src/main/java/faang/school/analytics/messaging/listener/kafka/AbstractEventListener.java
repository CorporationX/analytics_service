package faang.school.analytics.messaging.listener.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventService analyticsEventService;

    protected T eventHandler(String message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message, eventType);
        } catch (JsonProcessingException e) {
            log.error("error when deserializing an object {}, {}", message, eventType);
            throw new RuntimeException(e);
        }
    }
}
