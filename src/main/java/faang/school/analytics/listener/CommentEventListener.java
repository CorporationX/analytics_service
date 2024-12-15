package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.analytic.AnalyticsEventService;
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
            analyticsEventService.saveAction(analyticsEvent);
        } catch (IOException e) {
            log.error("Error reading value");
            throw new RuntimeException(e);
        }
    }
}
