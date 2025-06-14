package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.errorMessage.ErrorMessage;
import faang.school.analytics.exception.CommentEventDeserializationException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsService;
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
    private final AnalyticsService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent commentEvent;
        try {
            commentEvent = objectMapper.readValue(message.getBody(), CommentEvent.class);
            log.info("Received comment event: {}", commentEvent);

            AnalyticsEventDto analyticsEventDto = analyticsEventMapper.fromCommentEvent(commentEvent);

            analyticsEventService.saveEvent(analyticsEventDto);
            log.info("Saved analytics event from comment: {}", analyticsEventDto);

        } catch (IOException e) {
            String errorMessage = ErrorMessage.formatDeserializationError(e.getClass().getSimpleName() + ": " + e.getMessage());
            log.error(errorMessage, e);
            throw new CommentEventDeserializationException(errorMessage, e);
        } catch (Exception e) {
            log.error("Error processing comment event: {}", message.toString(), e);
        }
    }
}
