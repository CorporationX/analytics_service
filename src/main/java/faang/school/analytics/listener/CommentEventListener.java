package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.CommentEvent;
import faang.school.analytics.mapper.AnalyticEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener implements MessageListener {
    private final ObjectMapper mapper;
    private final AnalyticEventMapper analyticEventMapper;
    private final AnalyticsEventService analyticEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEvent commentEvent = mapper.readValue(message.getBody(), CommentEvent.class);
            AnalyticsEvent analyticsEvent = analyticEventMapper.toEntity(commentEvent);
            analyticsEvent.setEventType(EventType.POST_COMMENT);
            analyticEventService.saveAnalyticsEvent(analyticsEvent);
        } catch (IOException e) {
            String msg = "Exception occurred while parsing comment: " + e.getMessage();
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }
}
