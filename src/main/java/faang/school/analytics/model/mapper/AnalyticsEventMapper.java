package faang.school.analytics.model.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);
}
