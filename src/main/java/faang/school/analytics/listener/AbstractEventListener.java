package faang.school.analytics.listener;

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
    protected AnalyticsEventMapper analyticsEventMapper;
    @Autowired
    protected AnalyticsEventService analyticsEventService;
    private final Class<T> eventType;

    public AbstractEventListener(Class<T> eventType) {
        this.eventType = eventType;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        try {
            log.info("Received message from channel {}: {}", channel, body);
            T event = objectMapper.readValue(body, eventType);
            saveEvent(event);
        } catch (IOException e) {
            log.error("Failed to process message from channel {}: {}", channel, body, e);
            throw new RuntimeException("Error processing JSON message: " + e.getMessage(), e);
        }
    }

    protected abstract void saveEvent(T event);
}