package faang.school.analytics.mapper;

import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "authorPostId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(target = "eventType", constant = "POST_LIKE")
    @Mapping(target = "receivedAt", expression = "java(java.time.LocalDateTime.now())")
    AnalyticsEvent toEntity(LikeEvent likeEvent);

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(target = "eventType", constant = "GOAL_COMPLETED")
    @Mapping(target = "receivedAt", expression = "java(java.time.LocalDateTime.now())")
    AnalyticsEvent toEntity(GoalCompletedEvent likeEvent);

    AnalyticsEvent toEntity(SearchAppearanceEvent searchAppearanceEvent, String eventType);

    AnalyticsEvent toEntity(ProfileVeiwEvent profileVeiwEvent, String eventType);
}
