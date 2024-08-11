package faang.school.analytics.event;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class PostViewEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostViewEvent postViewEvent = objectMapper.readValue(message.getBody(), PostViewEvent.class);
            AnalyticsEvent analyticsEvent = new AnalyticsEvent();
            analyticsEvent.setEventType(EventType.POST_VIEW);
            analyticsEvent.setActorId(postViewEvent.getOwnerId());
            analyticsEvent.setReceiverId(postViewEvent.getPostId());
            analyticsEvent.setReceivedAt(postViewEvent.getPostViewTime());
            System.out.println(postViewEvent.getPostViewTime());
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
