package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.GenericEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.SerializationException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
abstract public class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final GenericEventMapper genericEventMapper;

    protected abstract Class<T> getEventType();
    protected T mapEvent(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            log.error("Couldn't read value from message: {}, at Event Type: {}", message.getBody(), eventType.getName());
            throw new SerializationException("Couldn't serialize message", e);
        }
    }
    @Override
    public void onMessage(Message message, byte[] pattern) {
        T eventFromMessage = mapEvent(message, getEventType());
        AnalyticsEventDto eventToSave = genericEventMapper.toEntity(eventFromMessage);
        analyticsEventService.saveEvent(eventToSave);
    }
}