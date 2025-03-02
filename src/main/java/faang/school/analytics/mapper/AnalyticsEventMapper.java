package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = EventType.class
)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(target = "receiverId", source = "event.postId")
    @Mapping(target = "receivedAt", source = "event.createdAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actorId", source = "event.userId")
    @Mapping(target = "eventType", source = "eventType")
    AnalyticsEvent toEntity(CommentEvent event, EventType eventType);

    @Mapping(target = "receiverId", source = "authorId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "timeStamp")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.POST_LIKE)")
    AnalyticsEvent toEntity(LikeEvent likeEvent);
}
