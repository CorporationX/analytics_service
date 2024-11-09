package faang.school.analytics.mapper;

import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.CommentEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsCommentEventMapper {

    @Mapping(target = "eventType", source = "postId", qualifiedByName = "eventType")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(CommentEvent commentEvent);

    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> events);

    @Named(value = "eventType")
    default EventType getEventType(long postId) {
        return EventType.POST_COMMENT;
    }
}
