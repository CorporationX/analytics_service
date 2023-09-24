package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.redis.events.LikeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticEventDto analyticEventDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receiverId", source = "postAuthorId")
    @Mapping(target = "actorId", source = "likeAuthorId")
    @Mapping(target = "eventType", expression = "java(getLikeEventType())")
    @Mapping(target = "receivedAt", source = "createdAt")
    AnalyticsEvent likeEventToAnalyticsEvent(LikeEvent likeEvent);

    default EventType getLikeEventType() {
        return EventType.POST_LIKE;
    }
}
