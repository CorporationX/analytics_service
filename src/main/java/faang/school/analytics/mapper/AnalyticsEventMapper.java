package faang.school.analytics.mapper;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toAnalyticsEvent(AnalyticsEventDto analyticsEventDto);
    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> events);
}
