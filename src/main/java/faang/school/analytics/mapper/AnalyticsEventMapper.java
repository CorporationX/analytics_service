package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.comment.CommentEvent;
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

    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "eventType", source = "eventType.eventType")
    AnalyticsEvent toEntity(CommentEvent event);
}
