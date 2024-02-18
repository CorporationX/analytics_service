package faang.school.analytics.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.base.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final List<AnalyticsEventMapper<T>> analyticsEventMappers;


    public void saveAnalyticsEvent(Message message, Class<T> type) {
        try {
            T t = objectMapper.readValue(message.getBody(), type);

            AnalyticsEvent analyticsEvent = analyticsEventMappers.stream().filter(analyticsEventMapper -> analyticsEventMapper.getType() == type)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Mapper not found for given type " + type.getName()))
                    .toAnalyticsEvent(t);

            analyticsEvent.setReceivedAt(LocalDateTime.now());

            analyticsEventService.saveAnalyticsEvent(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
