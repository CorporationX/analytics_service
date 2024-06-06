package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostViewEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String chanel = new String(message.getChannel());
            String body = new String(message.getBody());
            log.info("Received message from channel {}: {}", chanel, body);
            PostViewEvent postViewEvent = objectMapper.readValue(body, PostViewEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(postViewEvent);
            analyticsEvent.setEventType(EventType.POST_VIEW);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
