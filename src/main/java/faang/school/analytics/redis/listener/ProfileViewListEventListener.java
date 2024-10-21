package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProfileViewListEventListener extends AbstractListEventListener<ProfileViewEventDto> {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public ProfileViewListEventListener(
            ObjectMapper objectMapper,
            AnalyticsEventMapper analyticsEventMapper,
            AnalyticsEventService analyticsEventService,
            @Value("${spring.data.redis.channel.profile-view-list}") String profileViewEventTopic) {

        super(objectMapper, new ChannelTopic(profileViewEventTopic));
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;

        if (profileViewEventTopic == null) {
            throw new IllegalArgumentException("ProfileView event channel cannot be null");
        }
    }

    @Override
    public void saveEvents(List<ProfileViewEventDto> events) {
        analyticsEventService.saveAllEvents(analyticsEventMapper.toAnalyticsEvents(events));
        log.info("{} user view events saved", events.size());
    }

    @Override
    public Class<ProfileViewEventDto> getEventType() {
        return ProfileViewEventDto.class;
    }
}