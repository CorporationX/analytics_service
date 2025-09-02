package faang.school.analytics.mapper;

import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Маппер для преобразования ивентов в {@link AnalyticsEvent}
 *
 * @author Linempy
 * @since 20.08.2025
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "eventType", expression = "java(getEventType(event))")
    @Mapping(target = "actorId", source = "requesterId")
    AnalyticsEvent toEntity(RecommendationEvent event);

    default EventType getEventType(Object event) {
        return EventType.getEventTypeForClass(event.getClass());
    }
}