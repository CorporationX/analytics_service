package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticEventDto toDto(AnalyticsEvent analyticsEvent);
    AnalyticsEvent toEntity(AnalyticEventDto analyticEventDto);
}
