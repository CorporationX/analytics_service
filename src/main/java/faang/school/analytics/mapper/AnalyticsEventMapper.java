package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.event.PostViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto dto);

    AnalyticsEventDto toDto(AnalyticsEvent entity);

    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> entities);

    List<AnalyticsEvent> toEntityList(List<AnalyticsEventDto> dtos);

    @Mapping(target = "receiverId", source = "authorId")
    @Mapping(target = "actorId", source = "viewerId")
    @Mapping(target = "eventType", constant = "POST_VIEW")
    @Mapping(target = "receivedAt", source = "viewedAt")
    AnalyticsEventDto toDto(PostViewEventDto dto);
}
