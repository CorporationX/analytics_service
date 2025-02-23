package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toAnalyticsEventEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "commentAuthorId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(EventType.POST_COMMENT)")
    @Mapping(source = "commentedAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(CommentEvent commentEvent);

}
