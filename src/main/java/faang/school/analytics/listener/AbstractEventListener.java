package faang.school.analytics.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final AnalyticsService analyticsService;
    protected final AnalyticsEventMapper analyticsEventMapper;
    private final Class<T> type;

    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            AnalyticsEvent analyticsEvent = process(event);
            analyticsService.saveEvent(analyticsEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract AnalyticsEvent process(T event);
}
