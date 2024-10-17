package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> {

    public ProfileViewEventListener(ObjectMapper objectMapper,
                                    AnalyticsEventMapper analyticsEventMapper,
                                    AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    protected EventType getEventType() {
        return EventType.PROFILE_VIEW;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent profileViewEvent = handleEvent(message, ProfileViewEvent.class);
        log.debug("Received profile view event: {}", profileViewEvent);
        sendAnalytics(profileViewEvent);
    }
}
