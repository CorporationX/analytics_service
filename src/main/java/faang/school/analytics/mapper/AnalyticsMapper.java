package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsCreateEventDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsMapper {

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);
    AnalyticsEvent toEntity(AnalyticsCreateEventDto analyticsCreateEventDto);
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
}
