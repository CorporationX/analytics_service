package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisMessageSub implements MessageListener {

    private final RedisTemplate<String, String> redisTemplate;

    @Async
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String jsonMessage = new String((byte[]) message.getBody());
        try {
            // Разобрать JSON с помощью Jackson
            Map<String, Object> data = new ObjectMapper().readValue(jsonMessage, Map.class);

            // Извлечь данные
            Long userId = (Long) data.get("userId");
            Long viewedProfileId = (Long) data.get("viewedProfileId");
            LocalDateTime timestamp = LocalDateTime.parse((String) data.get("timestamp"));

            // Обработать событие просмотра профиля
            log.info("Получено сообщение о просмотре профиля: userId=%d, viewedProfileId=%d, timestamp=%s\n",
                    userId, viewedProfileId, timestamp);

            // Выполнить действия, например, обновить статистику или отправить уведомление
            // ...

        } catch (JsonProcessingException e) {
            // Обработать ошибку JSON-парсинга
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
