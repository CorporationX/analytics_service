package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventService analyticsEventService;
    protected final AnalyticsEventMapper analyticsEventMapper;
    private final Class<T> type;

    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            AnalyticsEvent analyticsEvent = process(event);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("event processed successfully {}", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract AnalyticsEvent process(T event);
}
