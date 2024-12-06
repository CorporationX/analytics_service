package faang.school.analytics.mapper.event;

import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    AnalyticsEvent toEntity(EventDto eventDto);

    EventDto toDto(AnalyticsEvent analyticsEvent);
}
