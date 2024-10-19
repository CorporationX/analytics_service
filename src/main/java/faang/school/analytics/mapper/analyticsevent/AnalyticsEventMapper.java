package faang.school.analytics.mapper.analyticsevent;

import faang.school.analytics.model.event.FollowerEvent;
import faang.school.analytics.model.event.FundRaisedEvent;
import faang.school.analytics.model.event.GoalCompletedEvent;
import faang.school.analytics.model.event.LikeEvent;
import faang.school.analytics.model.dto.analyticsevent.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.event.MentorshipRequestedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receivedAt", source = "subscribedAt")
    AnalyticsEvent toEntity(FollowerEvent followerEvent);

    @Mapping(target = "receiverId", source = "postAuthorId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "likedTime")
    AnalyticsEvent toEntity(LikeEvent likeEvent);

    @Mapping(target = "receiverId", source = "goalId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "completedAt")
    AnalyticsEvent toEntity(GoalCompletedEvent goalCompletedEvent);

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "requestedAt", target = "receivedAt")
    AnalyticsEvent toEntity(MentorshipRequestedEvent mentorshipRequestedEvent);

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "donatedAt", target = "receivedAt")
    AnalyticsEvent toEntity(FundRaisedEvent fundRaisedEvent);
}
