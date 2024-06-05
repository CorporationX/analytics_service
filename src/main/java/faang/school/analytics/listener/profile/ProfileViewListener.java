package faang.school.analytics.listener.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.profile.ProfileViewEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.profile.ProfileViewEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class ProfileViewListener extends AbstractEventListener<ProfileViewEvent> {

    private final ProfileViewEventMapper profileViewEventMapper;
    private final AnalyticsService analyticsService;

    public ProfileViewListener(ObjectMapper objectMapper, ProfileViewEventMapper profileViewEventMapper, AnalyticsService analyticsService) {
        super(objectMapper);
        this.profileViewEventMapper = profileViewEventMapper;
        this.analyticsService = analyticsService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class, event -> {
            AnalyticsEvent analyticsEvent = profileViewEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.PROFILE_VIEW);
            analyticsService.save(analyticsEvent);
        });
    }
}