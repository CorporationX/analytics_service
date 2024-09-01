package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.exception.EntityDeserializeException;
import faang.school.analytics.mapper.ProfileViewEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final ProfileViewEventMapper profileViewEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());

        ProfileViewEvent profileViewEvent;
        try {
            profileViewEvent = objectMapper.readValue(body, ProfileViewEvent.class);
        } catch (IOException e) {
            throw new EntityDeserializeException(ProfileViewEvent.class, body, e);
        }

        AnalyticsEvent analyticsEvent = profileViewEventMapper.toAnalyticsEvent(profileViewEvent);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);
        analyticsEventService.save(analyticsEvent);
    }
}
