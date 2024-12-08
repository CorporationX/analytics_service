package faang.school.analytics.mapper;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventRequestDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEventRequestDto toRequestDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(target = "eventType", ignore = true)
    @Mapping(target = "receivedAt", ignore = true)
    AnalyticsEvent toEntity(AnalyticsEventRequestDto analyticsEventRequestDto);

    List<AnalyticsEventDto> toDto(List<AnalyticsEvent> analyticsEvents);

    List<AnalyticsEvent> toEntity(List<AnalyticsEventDto> analyticsEventDtos);
}
