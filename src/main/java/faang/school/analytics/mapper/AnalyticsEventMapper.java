package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent entity);

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "goalId", target = "actorId")
    @Mapping(constant = "GOAL_COMPLETED", target = "eventType")
    @Mapping(source = "eventTime", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(GoalCompletedEvent event);
}
