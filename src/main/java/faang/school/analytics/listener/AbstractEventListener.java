package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventService analyticsEventService;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final Class<T> classType;

    protected AnalyticsEvent handleMessage(Message message) {
        try {
            T event = objectMapper.readValue(message.getBody(), classType);
            return toAnalyticsEvent(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract AnalyticsEvent toAnalyticsEvent(T event);
}
