package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractListenerForAddEvent<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String stringMessage = new String(message.getBody());
        try {
            log.warn("mapping message to " + getEventClass().getSimpleName());
            T event = objectMapper.readValue(stringMessage, getEventClass());
            handleEvent(event);
        } catch (JsonProcessingException e) {
            log.error("Пришел не верный формат данных в AbstractListenerForAddEvent",e);
            throw new IllegalArgumentException("Пришел не верный формат данных в AbstractListenerForAddEvent",e);
        }
    }

    public abstract Class<T> getEventClass();

    public abstract void handleEvent(T event);
}
