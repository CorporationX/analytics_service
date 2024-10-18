package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.function.Function;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper mapper;

    protected void saveEvent(Message message, Class<T> cl, Function<T, AnalyticsEvent> analyticsEventFunction) {
        try {
            T eventDto = objectMapper.readValue(message.getBody(), cl);
            AnalyticsEvent event = analyticsEventFunction.apply(eventDto);
            analyticsEventService.saveEvent(event);
            log.info("Event saved {}", event);
        } catch (IOException e) {
            log.error("Object mapper unread {} message error:", cl.getName(), e);
            e.printStackTrace();
        }
    }
}
