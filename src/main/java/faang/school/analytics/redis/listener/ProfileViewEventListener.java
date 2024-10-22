package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEventDto> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public ProfileViewEventListener(
            ObjectMapper objectMapper,
            AnalyticsEventService analyticsEventService,
            AnalyticsEventMapper analyticsEventMapper,
            @Value("${spring.data.redis.channel.profile-view}") String profileViewEventChannel) {

        super(objectMapper, new ChannelTopic(profileViewEventChannel));
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;

        if (profileViewEventChannel == null) {
            throw new IllegalArgumentException("ProfileView event channel cannot be null");
        }
    }

    @Override
    public void saveEvent(ProfileViewEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("Event saved: {}", event);
    }

    @Override
    public Class<ProfileViewEventDto> getEventType() {
        return ProfileViewEventDto.class;
    }
}
