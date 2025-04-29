package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.LikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Получено событие liked_post_topic: {}", new String(message.getBody()));
        try {
            LikeEvent event = objectMapper.readValue(message.getBody(), LikeEvent.class);
            analyticsEventService.handleLikeEvent(event);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка обработки сообщения Redis", e);
        }
    }
}
