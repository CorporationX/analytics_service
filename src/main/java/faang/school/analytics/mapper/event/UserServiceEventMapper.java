package faang.school.analytics.mapper.event;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserServiceEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actorId", source = "goalId")
    @Mapping(target = "eventType", expression = "java(setEventType())")
    @Mapping(target = "receivedAt", source = "time")
    AnalyticsEvent goalCompleteToAnalytics(GoalCompletedEvent event);

    default EventType setEventType() {
        return EventType.GOAL_COMPLETED;
    }
}
