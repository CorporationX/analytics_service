package faang.school.analytics.mapper;

import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.dto.SearchAppearanceEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto analyticEventDto);
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
    AnalyticsEvent fromSearchAppearanceToEntity(SearchAppearanceEvent searchAppearanceEvent);
}