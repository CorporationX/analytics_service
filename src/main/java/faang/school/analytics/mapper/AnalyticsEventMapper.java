package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {
    AnalyticsEvent toAnalyticsEvent(AnalyticsEventDto analyticsEventDto);

    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    List<AnalyticsEvent> toAnalyticsEventList(List<AnalyticsEventDto> analyticsEventDtoList);

    List<AnalyticsEventDto> toAnalyticsEventDtoList(List<AnalyticsEvent> analyticsEventList);
}
