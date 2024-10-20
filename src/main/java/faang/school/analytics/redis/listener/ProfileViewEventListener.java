package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileViewEventListener extends AbstractEventListenerList<ProfileViewEventDto> {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public ProfileViewEventListener(ObjectMapper javaTimeModuleObjectMapper,
                                    Topic profileViewEventTopic,
                                    AnalyticsEventService analyticsEventService,
                                    AnalyticsEventMapper analyticsEventMapper) {
        super(javaTimeModuleObjectMapper, profileViewEventTopic);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public void saveEvent(ProfileViewEventDto event) {
        analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEvent(event));
    }

    @Override
    public Class<ProfileViewEventDto> getEventType() {
        return ProfileViewEventDto.class;
    }

    @Override
    protected String getEventTypeName() {
        return "Profile view events";
    }
}
