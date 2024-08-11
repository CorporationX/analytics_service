package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import jakarta.annotation.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent entity);

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "goalId", target = "actorId")
    @Mapping(source = "eventType", target = "eventType" , qualifiedByName = "goalCompletedMapping")
    @Mapping(source = "eventTime", target = "receivedAt")
    AnalyticsEvent goalCompletedToAnalyticsEvent(GoalCompletedEvent event);

    @Named("goalCompletedMapping")
    default EventType goalMapping(@Nullable String str){
        return EventType.GOAL_COMPLETED;
    }
}
