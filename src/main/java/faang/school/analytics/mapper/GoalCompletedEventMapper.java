package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.GoalCompletedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoalCompletedEventMapper {

    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "goalId")
    @Mapping(target = "receivedAt", source = "completedAt")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.GOAL_COMPLETED)")
    AnalyticsEventDto toAnalyticsDto(GoalCompletedEvent event);
}

