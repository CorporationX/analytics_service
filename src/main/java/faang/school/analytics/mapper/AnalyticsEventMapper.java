package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = EventType.class)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto dto);

    AnalyticsEventDto toDto(AnalyticsEvent entity);

    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> entities);

    List<AnalyticsEvent> toEntityList(List<AnalyticsEventDto> dtos);

    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(target = "eventType", constant = "POST_COMMENT")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEventDto fromCommentEvent(CommentEvent commentEvent);
}
