package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public abstract class AbstractEventListener<T> implements MessageListener {

    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventService analyticsEventService;
    protected final AnalyticsEventMapper analyticsEventMapper;
    private final Class<T> eventType;

    @Autowired
    public AbstractEventListener(Class<T> eventType, ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        this.eventType = eventType;
        this.objectMapper = objectMapper;
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            saveEvent(event);
        } catch (IOException e) {
            log.error("Mapping error {}", message);
            throw new RuntimeException("Mapping error " + message);
        }
    }

    protected abstract void saveEvent(T event);
}