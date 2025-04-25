package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.EventTypeDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(target = "receivedAt", source = "timestamp")
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toAnalyticsEvent(CommentEvent commentEvent);

    @Mapping(target = "eventTypeDto", source = "eventType")
    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    List<AnalyticsEvent> toAnalyticsEventList(List<AnalyticsEventDto> analyticsEventDtoList);

    List<AnalyticsEventDto> toAnalyticsEventDtoList(List<AnalyticsEvent> analyticsEventList);

    default EventTypeDto eventTypeToDto(EventType eventType) {
        if (eventType == null) {
            return null;
        }
        return new EventTypeDto(eventType.name());
    }

    default EventType dtoToEventType(EventTypeDto eventTypeDto) {
        return EventType.valueOf(eventTypeDto.getName());
    }
}
