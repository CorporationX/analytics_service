package faang.school.analytics.mapper;

import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(SearchAppearanceEventDto searchAppearanceEventDto);

    SearchAppearanceEventDto toDto(AnalyticsEvent analyticsEvent);
}