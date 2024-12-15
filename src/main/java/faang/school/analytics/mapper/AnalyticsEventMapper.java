package faang.school.analytics.mapper;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventResponseDto;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.event.NewCommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventResponseDto entityToResponseDto(AnalyticsEvent event);

    AnalyticsEvent toEntity(AnalyticsEventResponseDto dto);

    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.POST_COMMENT)")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent newCommentEventToEntity(NewCommentEvent newCommentEvent);

    @Mapping(source = "goalId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.GOAL_COMPLETED)")
    @Mapping(source = "completedAt", target = "receivedAt")
    AnalyticsEvent goalCompletedEventToEntity(GoalCompletedEvent dto);
}

