package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {
    public ProfileViewEventListener(ObjectMapper objectMapper,
                                    AnalyticsEventService analyticsEventService,
                                    AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper, ProfileViewEvent.class);
    }

    @Override
    public AnalyticsEvent process(ProfileViewEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticEvent(event);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);
        return analyticsEvent;
    }
}
