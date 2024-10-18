package faang.school.analytics.mapper.analytics;

import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.dto.event.AbstractEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toAnalyticsEntity(AbstractEvent event);
}
