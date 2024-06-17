package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {

    protected ObjectMapper objectMapper;
    protected Class<T> eventClassType;
    protected AnalyticsEventService analyticsEventService;
    protected AnalyticsEventMapper analyticsEventMapper;

    public AbstractEventListener(ObjectMapper objectMapper,
                                 Class<T> eventClassType,
                                 AnalyticsEventService analyticsEventService,
                                 AnalyticsEventMapper analyticsEventMapper) {
        this.objectMapper = objectMapper;
        this.eventClassType = eventClassType;
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    protected T getEvent(Message message) {
        T event;
        try {
            event = objectMapper.readValue(message.getBody(), eventClassType);
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
        return event;
    }
}