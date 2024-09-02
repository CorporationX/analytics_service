package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final Class<T> eventType;

    protected abstract AnalyticsEvent toAnalyticsEvent(T dto);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            AnalyticsEvent analyticsEvent = toAnalyticsEvent(event);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (IOException e) {
            log.error("Error while processing message: {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
