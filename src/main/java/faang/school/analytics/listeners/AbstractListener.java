package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.services.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractListener <T> {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper mapper;
    public <T> void handleEvent(Message message,
                                Class<T> type,
                                Function<T, AnalyticsEvent> mappingToAnalyticsEvent){

        T event;
        try {
            event = mapper.readValue(message.getBody(), type);
        } catch (IOException e) {
            log.error("Deserialization failed", e);
            throw new RuntimeException(e);
        }
        //how can I here integrate generic mapper interface properly not using Function<> ?
        AnalyticsEvent analyticsEvent = mappingToAnalyticsEvent.apply(event);
        analyticsEventService.saveAnalyticsEvent(analyticsEvent);
        log.info("Data successfully passed to analyticsEventService");
    }
}
