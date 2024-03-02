package faang.school.analytics.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.base.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.EventListener;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<EventType> implements EventListener, MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper<EventType> analyticsEventMapper;

    protected void saveAnalyticsEvent(Message message, Class<EventType> type) {
        try {
            EventType event = objectMapper.readValue(message.getBody(), type);

            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
            log.debug("Analytics event was created: {}", analyticsEvent);
            analyticsEventService.save(analyticsEvent);
        } catch (IOException e) {
            log.debug("Analytics event was not created cause of IOException");
            throw new RuntimeException(e);
        }
    }
}