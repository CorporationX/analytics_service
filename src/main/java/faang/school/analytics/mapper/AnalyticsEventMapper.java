package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toAnalyticsEvent(AnalyticsEventDto analyticsEventDto);

    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.POST_COMMENT)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "receiverId", source = "postId")
    AnalyticsEvent toEntity(CommentEventDto commentEventDto);
}
