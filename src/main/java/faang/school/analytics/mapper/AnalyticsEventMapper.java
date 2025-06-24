package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toEntity(AnalyticsEventDto dto);

    AnalyticsEventDto toDto(AnalyticsEvent event);
}
