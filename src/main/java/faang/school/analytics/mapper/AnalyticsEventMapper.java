package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {
    AnalyticsEventDTO toDto(AnalyticsEvent analyticsEvent);
    AnalyticsEvent toEntity(AnalyticsEventDTO analyticsEventDTO);
}
