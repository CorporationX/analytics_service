package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto searchAppearanceEventDto);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
}