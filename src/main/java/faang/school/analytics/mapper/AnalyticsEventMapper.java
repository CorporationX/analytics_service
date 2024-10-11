package faang.school.analytics.mapper;


import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toAnalyticsEventsDto(AnalyticsEvent event);

    List<AnalyticsEventDto> toAnalyticsEventDtoList(List<AnalyticsEvent> events);
}
