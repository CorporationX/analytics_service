package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.CommentEventDto;
import faang.school.analytics.dto.event.ResponseAnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "postId", target = "receiverId")
    @Mapping(constant = "POST_COMMENT", target = "eventType")
    @Mapping(source = "dateTime", target = "receivedAt")
    AnalyticsEvent toEntity(CommentEventDto commentEventDto);

    ResponseAnalyticsEventDto toResponseAnalyticsEventDto(AnalyticsEvent analyticsEvent);
}
