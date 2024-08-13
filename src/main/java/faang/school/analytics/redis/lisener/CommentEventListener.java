package faang.school.analytics.redis.lisener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.CommentEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentEventListener implements MessageListener {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        System.out.println("Received message from channel " + channel + ": " + body);

        CommentEvent commentEvent = new CommentEvent();
        try {
            commentEvent = objectMapper.readValue(body, CommentEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        AnalyticsEvent analyticsEvent = analyticsEventMapper.commentEventToAnalyticsEvent(commentEvent);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}