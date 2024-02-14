package faang.school.analytics.mapper;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalCompletedEventMapper {
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.GOAL_COMPLETED)")
    @Mapping(target = "receivedAt", source = "timestamp")
    @Mapping(target = "receiverId", source = "userId")
    //@Mapping(target = "actorId", source = "goalId") under the question
    AnalyticsEvent toAnalyticsEvent(GoalCompletedEvent goalCompletedEvent);
}
