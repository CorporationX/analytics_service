package faang.school.analytics.mapper.analytics_event;

import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.event.FundRaisedEvent;
import faang.school.analytics.event.PremiumBoughtEvent;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.event.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventResponseDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "donatedAt", target = "receivedAt")
    AnalyticsEvent toEntity(FundRaisedEvent fundRaisedEvent);

    @Mapping(source = "projectId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "eventTime", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(ProjectViewEvent projectViewEvent);

    @Mapping(source = "goalId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "completedAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(GoalCompletedEvent goalCompletedEvent);

    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "followerId", target = "actorId")
    AnalyticsEvent toAnalyticsEvent(FollowerEvent followerEvent);

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "searchingUserId", target = "actorId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEventFromSearchAppearance(Long userId, Long searchingUserId, LocalDateTime viewedAt);

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "purchaseDate", target = "receivedAt")
    AnalyticsEvent toEntity(PremiumBoughtEvent premiumBoughtEvent);

    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "commentAuthorId", target = "actorId")
    @Mapping(source = "commentedAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(CommentEvent commentEvent);

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "recipientId", target = "receiverId")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(RecommendationEvent recommendationEvent);
}
