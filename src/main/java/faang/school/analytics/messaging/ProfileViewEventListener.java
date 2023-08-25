package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.messaging.events.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent profileViewEvent;
        try {
            profileViewEvent = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AnalyticsEvent analyticsEvent = analyticsEventMapper.viewProfileToAnalyticsEvent(profileViewEvent);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
