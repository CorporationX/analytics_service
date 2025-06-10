package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {
    @Mapping(target = "eventType", source = "eventType")
    AnalyticsEventDto toDto(AnalyticsEvent event);

    default String map(EventType eventType) {
        return eventType != null ? eventType.name() : null;
    }
}
