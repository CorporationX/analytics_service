package faang.school.analytics.redis.lisener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.CommentEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentEventListener implements MessageListener {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public static List<String> messageList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        messageList.add(message.toString());
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        System.out.println("Received message from channel " + channel + ": " + body);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        CommentEvent commentEvent = new CommentEvent();
        try {
            commentEvent = objectMapper.readValue(body, CommentEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        AnalyticsEvent analyticsEvent = analyticsEventMapper.commentEventToAnalyticsEvent(commentEvent);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        analyticsEvent.setReceivedAt(LocalDateTime.now());
        analyticsEventService.saveEvent(analyticsEvent);
    }
}