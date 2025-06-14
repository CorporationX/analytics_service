package faang.school.analytics.mapper;

import java.util.List;

import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import faang.school.analytics.dto.CommentEvent;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;

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
    @Mapping(source = "autorId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(EventType.POST_COMMENT.name())")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEventDto fromCommentEvent(CommentEvent comentEvent);
}
