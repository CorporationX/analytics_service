package faang.school.analytics.messaging.listener.postview;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.postview.PostViewEventDto;
import faang.school.analytics.exception.event.DataTransformationException;
import faang.school.analytics.exception.ExceptionMessages;
import faang.school.analytics.mapper.postview.PostViewEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostViewEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final PostViewEventMapper postViewEventMapper;
    private final ObjectMapper objectMapper;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message: {}", message.getBody());
        try {
            PostViewEventDto postViewEvent = objectMapper.readValue(message.getBody(), PostViewEventDto.class);
            AnalyticsEvent analyticsEvent = postViewEventMapper.toEntity(postViewEvent);
            analyticsEvent.setEventType(EventType.POST_VIEW);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (Exception e) {
            log.error(ExceptionMessages.INVALID_TRANSFORMATION, e);
            throw new DataTransformationException(ExceptionMessages.INVALID_TRANSFORMATION, e);
        }
    }
}