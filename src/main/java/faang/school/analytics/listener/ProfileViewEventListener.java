package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> {

    private final AnalyticsEventService analyticsEventService;

    @Autowired
    public ProfileViewEventListener(ObjectMapper objectMapper,
                                    AnalyticsEventService analyticsEventService) {
        super(objectMapper);
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent profileViewEvent = getEvent(message, ProfileViewEvent.class);
        analyticsEventService.saveProfileView(profileViewEvent);
    }
}
