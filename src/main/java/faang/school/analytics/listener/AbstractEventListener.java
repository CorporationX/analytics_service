package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

/**
 * Абстрактный базовый класс для обработки событий из Redis.
 * Предоставляет общую логику десериализации сообщений и сохранения аналитических событий.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    private final Class<T> eventType;

    /**
     * Обрабатывает входящее сообщение из Redis.
     * Десериализует тело сообщения в объект указанного типа и передает его на обработку.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            handleEvent(event);
        } catch (IOException e) {
            log.error("Failed to deserialize message", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Абстрактный метод для обработки конкретного события.
     * Должен быть реализован в классах-наследниках.
     */
    protected abstract void handleEvent(T event);
}