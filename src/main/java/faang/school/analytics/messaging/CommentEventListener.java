package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.CommentEventDto;
import faang.school.analytics.exception.MessageConversionException;
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
public class CommentEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Parsing incoming message");
        CommentEventDto dto;
        try {
            dto = objectMapper.readValue(message.getBody(), CommentEventDto.class);
        } catch (IOException e) {
            throw new MessageConversionException("Message conversion resulted in error: " + e);
        }
        log.info("Converting message to analytics event");
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(dto);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        log.info("Saving analytics event");
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("Analytics event saved");
    }
}