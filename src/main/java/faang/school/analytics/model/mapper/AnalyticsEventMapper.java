package faang.school.analytics.model.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(source = "eventType", target = "eventType", qualifiedByName = "eventTypeToString")
    @Mapping(source = "receivedAt", target = "receivedAt", qualifiedByName = "localDateTimeToString")
    AnalyticsEventDto toDto(AnalyticsEvent event);

    @Mapping(source = "eventType", target = "eventType", qualifiedByName = "stringToEventType")
    @Mapping(source = "receivedAt", target = "receivedAt", qualifiedByName = "stringToLocalDateTime")
    AnalyticsEvent toEntity(AnalyticsEventDto dto);

    @Named("eventTypeToString")
    static String eventTypeToString(EventType eventType) {
        return eventType != null ? eventType.name() : null;
    }

    @Named("stringToEventType")
    static EventType stringToEventType(String eventType) {
        return eventType != null ? EventType.valueOf(eventType) : null;
    }

    @Named("localDateTimeToString")
    static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    @Named("stringToLocalDateTime")
    static LocalDateTime stringToLocalDateTime(String dateTime) {
        return dateTime != null ? LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
}
