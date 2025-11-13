package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.dto.CommentEventDto;
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

    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.POST_COMMENT)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receiverId", source = "postAuthorId")
    AnalyticsEvent toEntity(CommentEventDto commentEventDto);

    default AnalyticsEvent toEntity(Object eventDto) {
        if (eventDto instanceof EventDto) {
            return toEntity((EventDto) eventDto);
        } else if (eventDto instanceof CommentEventDto) {
            return toEntity((CommentEventDto) eventDto);
        }
        throw new IllegalArgumentException("Unsupported event DTO type: " + eventDto.getClass().getName());
    }
}
