package faang.school.analytics.listner.abstract_listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.base.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

@Slf4j
public abstract class AbstractEventListener<T> {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AnalyticsEventService analyticsEventService;
    @Autowired
    private List<AnalyticsEventMapper<T>> analyticsEventMappers;

    protected void saveAnalyticsEvent(Message message, Class<T> type) {
        try {
            T t = objectMapper.readValue(message.getBody(), type);

            AnalyticsEvent analyticsEvent = analyticsEventMappers.stream()
                    .filter(analyticsEventMapper -> analyticsEventMapper.getType() == type)
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException("Mapper not found for given type " + type.getName()))
                    .toAnalyticsEvent(t);
            log.debug("Analytics event was created: {}", analyticsEvent);
            analyticsEventService.save(analyticsEvent);
        } catch (IOException e) {
            log.debug("Analytics event was not created cause of IOException");
            throw new RuntimeException(e);
        }
    }
}
