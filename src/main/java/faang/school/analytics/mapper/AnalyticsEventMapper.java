package faang.school.analytics.mapper;

import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(SearchAppearanceEventDto searchAppearanceEventDto);

    SearchAppearanceEventDto toDto(AnalyticsEvent analyticsEvent);
}