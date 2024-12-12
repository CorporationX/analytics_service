package faang.school.analytics.mapper;

import faang.school.analytics.event.RecommendationEvent;
import faang.school.analytics.event.follower.FollowerEvent;
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

    @Mapping(target = "eventType", constant = "RECOMMENDATION_RECEIVED")
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "receivedAt", source = "dateTime")
    AnalyticsEvent recommendationEventToAnalyticsEvent(RecommendationEvent recommendationEvent);
}
