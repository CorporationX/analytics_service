package faang.school.analytics.mapper;

import faang.school.analytics.event.follower.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.event.ProfileViewEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "id", target = "receiverId")
    @Mapping(source = "viewerID", target = "actorId")
    @Mapping(source = "viewingDateAndTime", target = "receivedAt")
    @Mapping(target = "eventType", constant = "PROFILE_VIEW")
    AnalyticsEvent profileViewEventToAnalyticsEvent(ProfileViewEvent profileViewEvent);

    @Mapping(target = "eventType", constant = "FOLLOWER")
    AnalyticsEvent followerEventToAnalyticsEvent(FollowerEvent followerEvent);
}
