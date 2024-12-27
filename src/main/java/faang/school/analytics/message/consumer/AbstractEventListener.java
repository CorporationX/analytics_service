package faang.school.analytics.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.EventProcessingException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.util.function.Function;

@Slf4j
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventService analyticsEventService;

    protected AbstractEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService) {
        this.objectMapper = objectMapper;
        this.analyticsEventService = analyticsEventService;
    }

    public void handleEvent(Message message, Class<T> eventType, Function<T, AnalyticsEvent> mapper) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            AnalyticsEvent analyticsEvent = mapper.apply(event);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("Processed and saved event: {}", event);
        } catch (Exception e) {
            log.error("Error processing event message", e);
            throw new EventProcessingException(eventType, e);
        }
    }
}
