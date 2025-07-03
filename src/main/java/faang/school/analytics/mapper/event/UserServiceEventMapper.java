package faang.school.analytics.mapper.event;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.dto.RecommendationReceivedEvent;
import faang.school.analytics.dto.premium.PremiumBoughtEvent;
import faang.school.analytics.dto.post.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserServiceEventMapper {

    @Mapping(target = "actorId", source = "goalId")
    @Mapping(target = "eventType", expression = "java(setEventType(\"GOAL_COMPLETED\"))")
    @Mapping(target = "receivedAt", source = "time")
    AnalyticsEvent goalCompleteToAnalytics(GoalCompletedEvent event);

    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "eventType", expression = "java(setEventType(\"RECOMMENDATION_RECEIVED\"))")
    @Mapping(target = "receivedAt", source = "createdAt")
    AnalyticsEvent recommendationReceivedToAnalytics(RecommendationReceivedEvent event);

    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "eventType", expression = "java(setEventType(\"PREMIUM_BOUGHT\"))")
    @Mapping(target = "receivedAt", source = "startDate")
    AnalyticsEvent premiumBoughtToAnalytics(PremiumBoughtEvent event);

    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "actorId", source = "viewerId")
    @Mapping(target = "eventType", expression = "java(setEventType(\"POST_VIEW\"))")
    @Mapping(target = "receivedAt", source = "createdAt")
    AnalyticsEvent postViewToAnalytics(PostViewEvent event);

    default EventType setEventType(String type) {
        return EventType.valueOf(type);
    }
}
