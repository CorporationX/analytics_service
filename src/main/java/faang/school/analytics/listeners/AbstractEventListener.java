package faang.school.analytics.listeners;

import java.io.IOException;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.analytics.config.redis.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements TopicProvider, MessageListener {
    private final ObjectMapper objectMapper;
    protected final RedisProperties redisProperties;
    
    protected T deserializeEvent(Message message) {
        try {
            return objectMapper.readValue(message.getBody(), getPayloadClass());
        } catch (IOException e) {
            String error = "Can't deserialize event";
            log.error(error, e);
            throw new RuntimeException(error, e);
        }
    }
    
    public void onMessage(Message message, byte[] pattern) {
        T event = deserializeEvent(message);
        onMessageAction(event);
    }
    
    public abstract void onMessageAction(T event);
    
    public abstract Class<T> getPayloadClass();
}
