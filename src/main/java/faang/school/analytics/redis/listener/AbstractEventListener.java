package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    private final ConcurrentLinkedQueue<T> eventQueue = new ConcurrentLinkedQueue<>();
    private final ObjectMapper objectMapper;
    @Getter
    private final Topic topic;

    public AbstractEventListener(ObjectMapper objectMapper, Topic topic) {
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public abstract void saveBatch(List<T> events);

    public abstract Class<T> getEventType();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            T event = objectMapper.readValue(messageBody, getEventType());
            eventQueue.add(event);
        } catch (Exception e) {
            log.error("Error processing message: {}", new String(message.getBody()), e);
        }
    }

    @Scheduled(fixedRateString = "${event.listener.flushInterval}")
    public void flushEventsPeriodically() {
        flushEvents();
    }

    private void flushEvents() {
        List<T> eventsToSave = new ArrayList<>();
        T event;
        while ((event = eventQueue.poll()) != null) {
            eventsToSave.add(event);
        }
        if (!eventsToSave.isEmpty()) {
            saveBatch(eventsToSave);
        }
    }
}