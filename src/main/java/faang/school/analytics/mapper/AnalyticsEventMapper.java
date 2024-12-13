package faang.school.analytics.mapper;

import faang.school.analytics.event.follower.FollowerEvent;
import faang.school.analytics.event.mentorship.MentorshipRequestedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "eventType", constant = "FOLLOWER")
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "receivedAt", expression = "java(java.time.LocalDateTime.now())")
    AnalyticsEvent followerEventToAnalyticsEvent(FollowerEvent followerEvent);

    @Mapping(target = "eventType", constant = "MENTORSHIP_REQUESTED")
    @Mapping(target = "actorId", source = "requesterId")
    @Mapping(target = "receivedAt", source = "requestedAt")
    AnalyticsEvent mentorshipRequestedEventToAnalyticsEvent(MentorshipRequestedEvent followerEvent);
}
