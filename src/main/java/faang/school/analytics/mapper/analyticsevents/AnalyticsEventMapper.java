package faang.school.analytics.mapper.analyticsevents;

import faang.school.analytics.model.dto.FollowerEventDto;
import faang.school.analytics.model.dto.GoalCompletedEventDto;
import faang.school.analytics.model.dto.analyticsevent.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receivedAt", source = "subscribedAt")
    AnalyticsEvent toEntity(FollowerEventDto followerEvent);

    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "likedTime")
    AnalyticsEvent toEntity(LikeEventDto likeEventDto);

    @Mapping(target = "receiverId", source = "goalId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "completedAt")
    AnalyticsEvent toEntity(GoalCompletedEventDto goalCompletedEvent);
}
