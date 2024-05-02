package faang.school.analytics.mapper;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(source = "followeeId", target = "actorId")
    @Mapping(source = "followerId", target = "receiverId")
    @Mapping(source = "subscriptionDateTime", target = "receivedAt")
    @Mapping(target = "eventType", constant = "FOLLOWER")
    AnalyticsEvent toAnalyticsEvent(FollowerEvent dto);

    @Mapping(source = "observerId", target = "actorId")
    @Mapping(source = "observedId", target = "receiverId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    @Mapping(target = "eventType", constant = "PROFILE_VIEW")
    AnalyticsEvent toAnalyticsEvent(ProfileViewEvent event);

    @Mapping(source = "viewedUserId", target = "receiverId")
    @Mapping(source = "viewerUserId", target = "actorId")
    @Mapping(source = "viewingTime", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(SearchAppearanceEvent searchAppearanceEvent);
}
