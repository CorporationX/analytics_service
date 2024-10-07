package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public abstract class AbstractListener<T> implements MessageListener {

    protected final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    protected final AnalyticsEventMapper analyticsEventMapper;

    protected AbstractListener(ObjectMapper objectMapper,
                               AnalyticsEventService analyticsEventService,
                               AnalyticsEventMapper analyticsEventMapper) {
        this.objectMapper = objectMapper;
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event;
        try {
            event = listenEvent(message);
        } catch (IOException e) {
            log.warn("Unsuccessful mapping", e);
            throw new MessageProcessingException("Error processing message", e);
        }
        AnalyticsEvent analyticsEvent = mapToAnalyticsEvent(event);
        saveAnalyticsEvent(analyticsEvent);
        log.info("Data successfully passed to analyticsEventService");
    }

    protected abstract T listenEvent(Message message) throws IOException;

    protected abstract AnalyticsEvent mapToAnalyticsEvent(T event);

    protected void saveAnalyticsEvent(AnalyticsEvent event) {
        analyticsEventService.saveAnalyticsEvent(event);
    }
}