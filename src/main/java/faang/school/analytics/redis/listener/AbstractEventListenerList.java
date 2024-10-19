package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.Topic;

import java.io.IOException;
import java.util.List;

@Slf4j
public abstract class AbstractEventListenerList<T> extends AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final Topic topic;

    public AbstractEventListenerList(ObjectMapper objectMapper, Topic topic) {
        super(objectMapper, topic);
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message from \"{}\" topic", topic.getTopic());
        try {
            List<T> eventDtoList = objectMapper.readValue(message.getBody(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, getEventType()));

            eventDtoList.forEach(this::saveEvent);
            log.info("{} {} saved", eventDtoList.size(), getEventTypeName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String getEventTypeName();
}
