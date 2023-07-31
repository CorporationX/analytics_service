package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsMapper {
    AnalyticDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticDto analyticDto);

    public AnalyticDto readValue(byte[] messageBody, Class targetClass);
}
