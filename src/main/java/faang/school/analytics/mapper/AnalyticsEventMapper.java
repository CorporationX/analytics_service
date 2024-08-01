package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent event);

    @Mapping(target = "actorId", source = "commentAuthorId")
    @Mapping(target = "eventType", ignore = true)
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "receiverId", source = "postId")
    AnalyticsEvent toEntity(CommentEventDto commentEventDto);
}
