package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.AbstractEvent;
import faang.school.analytics.mapper.analytics.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final AnalyticsEventService analyticsEventService;

    protected abstract EventType getEventType();

    protected AnalyticsEvent mapToAnalyticsEvent(AbstractEvent abstractEvent) {
        try {
            return analyticsEventMapper.toAnalyticsEntity(abstractEvent);
        } catch (Exception e) {
            log.error("Error mapping abstract event to AnalyticsEvent: {}", e.getMessage());
            throw new MappingException("Failed to map abstract event", e);
        }
    }

    protected void saveEvent(AnalyticsEvent event) {
        log.info("saveEvent() - start, event - {}", event);
        event.setEventType(getEventType());
        analyticsEventService.saveEvent(event);
        log.info("saveEvent() - finish, event - {}", event);
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            log.info("handleEvent() - start");
            T event = objectMapper.readValue(message.getBody(), type);
            log.debug("handleEvent() - event - {}", event);
            consumer.accept(event);
            log.info("handleEvent() - finish, event - {} ", event);
        } catch (IOException e) {
            log.error("handleEvent() - error - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
