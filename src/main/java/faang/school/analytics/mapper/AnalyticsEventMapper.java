package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> analyticsEvents);
}
