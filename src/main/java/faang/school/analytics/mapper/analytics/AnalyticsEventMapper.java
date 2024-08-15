package faang.school.analytics.mapper.analytics;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
    List<AnalyticsEventDto> toListDto(List<AnalyticsEvent> analyticsEventList);
}