package faang.school.analytics.mapper;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "eventType", constant = "GOAL_COMPLETED")
    @Mapping(target = "receivedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "id", ignore = true)
    AnalyticsEvent toAnalyticsEvent(GoalCompletedEvent goalCompletedEvent);
}