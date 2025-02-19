package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.messaging.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {

    private AnalyticsEventMapper analyticsEventMapper;
    private AnalyticsEventService analyticsEventService;
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEvent likeEvent = null;
        try {
            likeEvent = objectMapper.readValue(message.getBody(), LikeEvent.class);
        } catch (IOException e) {
            log.error("Произошла ошибка при конвертации сообщения из Json в LikeEvent: {}", e.getMessage());
            throw new RuntimeException("Произошла ошибка при конвертации сообщения из Json в LikeEvent", e);
        }

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(likeEvent);
        analyticsEvent.setEventType(EventType.POST_LIKE);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
