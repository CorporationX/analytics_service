package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
}
