package faang.school.analytics.redis.lisener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.ProfileViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        System.out.println("Received message from channel " + channel + ": " + body);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ProfileViewEvent profileViewEvent = objectMapper.readValue(body, ProfileViewEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.profileViewEventToAnalyticsEvent(profileViewEvent);
            analyticsEvent.setEventType(EventType.FOLLOWER);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
