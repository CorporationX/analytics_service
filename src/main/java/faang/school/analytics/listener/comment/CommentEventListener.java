package faang.school.analytics.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.mapper.events.AnalyticsEventMapper;
import faang.school.analytics.service.events.AnalyticsEventService;
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
        try {
            CommentEvent commentEvent = objectMapper.readValue(message.getBody(), CommentEvent.class);
            AnalyticsEventDto analyticsEvent = analyticsEventMapper.commentToAnalyticsDto(commentEvent);
            log.info("Saving comment event: {}", analyticsEvent);
            analyticsEventService.saveCommentEvent(analyticsEvent);
        } catch (IOException e) {
            log.error("Error reading value {}", message, e);
            throw new RuntimeException(e);
        }
    }
}
