package faang.school.analytics.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, 
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, 
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto dto);
    AnalyticsEventDto toDto(AnalyticsEvent entity);

    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> entities);
    List<AnalyticsEvent> toEntityList(List<AnalyticsEventDto> dtos);
}
