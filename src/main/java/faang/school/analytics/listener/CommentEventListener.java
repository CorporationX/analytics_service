package faang.school.analytics.listener;

import java.io.IOException;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper eventMapper;
    private final AnalyticsEventService analytivsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEvent commentEvent = objectMapper.readValue(message.getBody(), CommentEvent.class);
            CreateAnalyticsEventDto analyticsEvent = eventMapper.toDto(commentEvent);

            log.info("Received comment event: {}", commentEvent);

            analytivsEventService.saveEvent(analyticsEvent);

        } catch (IOException e) {
            log.error("Error creating comment event", e);
            throw new MessageProcessingException("Failed to process incoming message", e);
        }
    }
}
