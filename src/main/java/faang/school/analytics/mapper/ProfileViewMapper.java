package faang.school.analytics.mapper;

import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProfileViewMapper {
    @Mapping(target = "receiverId", source = "viewedUserId")
    @Mapping(target = "actorId", source = "viewingUserId")
    @Mapping(target = "receivedAt", source = "viewedAt")
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toAnalyticsEvent(ProfileViewEvent profileViewEvent);

    default void setEventType(AnalyticsEvent analyticsEvent) {
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);
    }
}
