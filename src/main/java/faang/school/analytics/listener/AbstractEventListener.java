package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final AnalyticsService analyticsService;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final Class<T> eventType;

    protected abstract AnalyticsEvent mapEvent(T event);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            AnalyticsEvent analyticsEvent = mapEvent(event);
            analyticsService.saveEvent(analyticsEvent);
        } catch (IOException e) {
            log.error("IOException was thrown", e);
            throw new SerializationException("Failed to deserialize a message", e);
        }
    }
}
