package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(target = "receivedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.valueOf(eventDto.eventType()))")
    AnalyticsEvent toEntity(EventDto eventDto);

    AnalyticsEventResponseDto toDto(AnalyticsEvent analyticsEvent);

    List<AnalyticsEventResponseDto> toDtoList(List<AnalyticsEvent> events);
}
