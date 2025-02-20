package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.dto.AnalyticsEventDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent event);

    AnalyticsEvent toEntity(AnalyticsEventDto eventDto);
}
