package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.LikeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "id", source = "postId")
    @Mapping(target = "receivedAt", source = "dateLike")
    AnalyticsEvent toEntity(LikeEvent likeEvent);

    @Mapping(target = "eventType", source = "postId", qualifiedByName = "eventType")
    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "actorId", source = "commentId")
    @Mapping(target = "receivedAt", source = "date")
    AnalyticsEvent toAnalytics(CommentEvent commentEvent);

    @Named(value = "eventType")
    default EventType getEventType(long postId) {
        return EventType.POST_COMMENT;
    }
}
