package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

/**
 * @author Alexander Bulgakov
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<E> implements MessageListener {
    private final ObjectMapper objectMapper;

    protected abstract TypeReference<E> getTypeReference();

    protected abstract void handleEvent(E event);
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            E event = objectMapper.readValue(messageBody, getTypeReference());
            handleEvent(event);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
