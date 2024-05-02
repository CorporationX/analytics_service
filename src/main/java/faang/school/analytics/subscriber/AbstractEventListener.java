package faang.school.analytics.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final AnalyticsMapper<T, AnalyticsEvent> mapper;
    private final AnalyticsEventService analyticsEventService;

    protected AnalyticsEvent readAndMapEvent(Message message, Class<T> type){
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            log.info("Received event: {}", event);
            return mapper.toAnalyticsEvent(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    protected void saveEventAnalytics(AnalyticsEvent analyticsEvent){
        analyticsEventService.saveEventAnalytics(analyticsEvent);
    }

}
