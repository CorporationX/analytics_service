package faang.school.analytics.mapper;

import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProfileViewEventMapper {

    public static AnalyticsEvent toAnalyticsEvent(ProfileViewEvent event) {
        return AnalyticsEvent.builder()
                .receiverId(event.userId())
                .actorId(event.viewerId())
                .receivedAt(event.viewedAt())
                .eventType(EventType.PROFILE_VIEW)
                .build();
    }
}

