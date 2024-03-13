package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected AnalyticsEventService analyticsEventService;
    @Autowired
    protected AnalyticsEventMapper eventMapper;
    private final Class<T> type;

    public AbstractEventListener(Class<T> type) {
        this.type = type;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event = deserialize(message.getBody());
        saveEvent(event);
        log.info("Event was successful save {}", event);
    }

    private T deserialize(byte[] body) {
        try {
            return objectMapper.readValue(body, type);
        } catch (Exception e) {
            log.error("Json unsuccessful convert to data");
            throw new RuntimeException(e.getMessage());
        }
    }

    protected abstract AnalyticsEvent mapEvent(T event);

    public abstract void saveEvent(T event);

}
