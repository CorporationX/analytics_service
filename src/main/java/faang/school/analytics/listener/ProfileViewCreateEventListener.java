package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;


@Component
public class ProfileViewCreateEventListener extends AbstractEventListener<ProfileViewEvent> {

    public ProfileViewCreateEventListener(
            ObjectMapper objectMapper,
            AnalyticsEventService analyticsEventService,
            AnalyticsEventMapper analyticsEventMapper
    ) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent event = getEventFromBytes(message.getBody(), ProfileViewEvent.class);
        var eventAnalytic = analyticsEventMapper.toAnalyticsFromUserProfileView(event);
        analyticsEventService.saveEvent(eventAnalytic);
    }
}