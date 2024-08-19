package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> analyticsEventList);
}
