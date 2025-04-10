package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.GoalCompletedEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoalCompletedEventMapper {

    AnalyticsEventDto toAnalyticsDto(GoalCompletedEvent event);
}
