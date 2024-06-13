package faang.school.analytics.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public abstract class AbstractEventListener<T> implements MessageListener {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected AnalyticsEventService analyticsEventService;
    @Autowired
    protected AnalyticsEventMapper analyticsEventMapper;
    private final Class<T> eventType;

    public AbstractEventListener(Class<T> eventType) {
        this.eventType = eventType;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            saveEvent(event);
        } catch (IOException e) {
            log.error("Mapping error {}", message);
            e.printStackTrace();
        }
    }

    protected abstract void saveEvent(T event);
}
