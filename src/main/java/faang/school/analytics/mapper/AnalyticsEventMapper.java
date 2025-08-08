package faang.school.analytics.mapper;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toAnalyticsEvent(AnalyticsEventDto analyticsEventDto);
    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);
    List<AnalyticsEventDto> toAnalyticsEventDtoList(List<AnalyticsEvent> analyticsEventList);
}

