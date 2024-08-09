package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "eventType", target = "eventType", qualifiedByName = "stringEventTypeToEnum")
    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "eventType", target = "eventType", qualifiedByName = "enumEventTypeToString")
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Named("enumEventTypeToString")
    default String enumEventTypeToString(EventType eventType){
        if (eventType == null) return null;
        return eventType.name().toUpperCase();
    }

    @Named("stringEventTypeToEnum")
    default EventType stringEventTypeToEnum(String eventType){
        if (eventType == null) return null;
        try {
            return EventType.valueOf(eventType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The value " + eventType + " does not correspond to any valid EventType.", e);
        }
    }
}