package faang.school.analytics.listeners;

import faang.school.analytics.exception.MessageError;
import faang.school.analytics.mappers.AbstractAnalyticsEventMapper;
import faang.school.analytics.mappers.JsonMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.services.AnalyticsEventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractListener <T> {
    private final List<AbstractAnalyticsEventMapper<T>> mappers;
    private final JsonMapper jsonMapper;
    private final AnalyticsEventService analyticsEventService;
    public <T> void handleEvent(Message message, Class<T> type, EventType eventType){
        T event = jsonMapper.toEvent(message.getBody(), type);
        AbstractAnalyticsEventMapper analyticsEventMapper = mappers.stream()
                .filter(mapper -> mapper.isSupported(type))
                .findFirst().orElseThrow(() -> new EntityNotFoundException(MessageError.EVENT_TYPE_MAPPER_NOT_FOUNT.getMessage()));
        AnalyticsEvent analyticsEvent = analyticsEventMapper.handleMapping(event, eventType);
        analyticsEventService.saveAnalyticsEvent(analyticsEvent);
        log.info("Data successfully passed to analyticsEventService");
    }
}
