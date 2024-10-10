package faang.school.analytics.mapper;

import faang.school.analytics.model.dto.GoalCompletedEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "receiverId", source = "goalId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "completedAt")
    AnalyticsEvent toEntity(GoalCompletedEventDto goalCompletedEvent);
}
