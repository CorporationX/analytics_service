package faang.school.analytics.mapper;

import faang.school.analytics.dto.GoalCompletedEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "goalId")
    @Mapping(target = "receivedAt", source = "goalCompletedAt")
    AnalyticsEvent toAnalyticsEvent(GoalCompletedEventDto goalCompletedEventDto);
}
