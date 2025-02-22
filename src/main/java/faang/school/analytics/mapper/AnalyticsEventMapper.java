package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.CommentEvent;
import faang.school.analytics.dto.goal.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "receiverId", source = "event.postId")
    @Mapping(target = "actorId", source = "event.postAuthorId")
    @Mapping(target = "eventType", constant = "COMMENT")
    @Mapping(target = "receivedAt", source = "event.date")
    AnalyticsEvent toAnalyticsEventFromCommentEvent(CommentEvent event);

    @Mapping(target = "receiverId", source = "event.goalId")
    @Mapping(target = "actorId", source = "event.userId")
    @Mapping(target = "eventType", constant = "GOAL_COMPLETED")
    @Mapping(target = "receivedAt", source = "event.date")
    AnalyticsEvent toAnalyticsEntityFromGoalCompletedEvent(GoalCompletedEvent event);
}
