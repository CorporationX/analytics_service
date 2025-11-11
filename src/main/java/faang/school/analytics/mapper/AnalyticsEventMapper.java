package faang.school.analytics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent model);
    
    AnalyticsEvent toModel(CreateAnalyticsEventDto dto);
}
