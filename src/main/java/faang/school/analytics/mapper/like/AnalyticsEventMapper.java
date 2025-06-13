package faang.school.analytics.mapper.like;

import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "eventType", expression = "java(setDefaultEventTypeToLike())")
    @Mapping(target = "receivedAt", source = "createdAt")
    AnalyticsEvent likeEventToAnalytics(LikeEvent event);

    default EventType setDefaultEventTypeToLike() {
        return EventType.POST_LIKE;
    }
}
