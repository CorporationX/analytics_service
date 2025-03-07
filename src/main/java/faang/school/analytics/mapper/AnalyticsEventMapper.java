package faang.school.analytics.mapper;

import faang.school.analytics.dto.ProfileViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventMapper {
    public AnalyticsEvent toAnalyticsEvent(ProfileViewEventDto eventDto) {
        return AnalyticsEvent.builder()
                .receiverId(eventDto.getProfileOwnerId())
                .actorId(eventDto.getViewerId())
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(eventDto.getViewedAt())
                .build();
    }
}
