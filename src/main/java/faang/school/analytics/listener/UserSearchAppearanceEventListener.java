package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.UserSearchAppearanceEvent;
import faang.school.analytics.mapper.event.UserSearchAppearanceEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.event.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class UserSearchAppearanceEventListener extends AbstractListenerForAddEvent<UserSearchAppearanceEvent> {
    private final UserSearchAppearanceEventMapper userSearchAppearanceEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public UserSearchAppearanceEventListener(ObjectMapper objectMapper, UserSearchAppearanceEventMapper userSearchAppearanceEventMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper);
        this.userSearchAppearanceEventMapper = userSearchAppearanceEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public Class<UserSearchAppearanceEvent> getEventClass() {
        return UserSearchAppearanceEvent.class;
    }

    @Override
    public void handleEvent(UserSearchAppearanceEvent event) {
        AnalyticsEvent analyticsEvent = userSearchAppearanceEventMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.PROFILE_APPEARED_IN_SEARCH);
        analyticsEventService.addNewEvent(analyticsEvent);
    }
}
