package faang.school.analytics.mapper.event;

import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalCompletedEventMapper {
    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "goalId", target = "actorId")
    @Mapping(source = "goalAchieveDateTime", target = "receivedAt")
    AnalyticsEvent toEntity(GoalCompletedEvent goalCompletedEvent);
}
