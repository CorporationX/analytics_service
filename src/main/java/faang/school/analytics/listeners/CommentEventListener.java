package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.AnalyticsMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {

    private final AnalyticsMapper analyticsMapper;

    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent event = null;
        try {
            event = objectMapper.readValue(message.getBody(), CommentEvent.class);
            AnalyticsEvent mapperAnalyticsEvent = analyticsMapper.toAnalyticsEvent(event);
            mapperAnalyticsEvent.setEventType(EventType.POST_COMMENT);
            AnalyticsEvent analyticsEvent = analyticsEventService.addAnalyticsEvent(mapperAnalyticsEvent);
            log.info("CommentEventListener CommentEvent: {}", event);
            log.info("CommentEventListener AnalyticsEvent: {}", analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
