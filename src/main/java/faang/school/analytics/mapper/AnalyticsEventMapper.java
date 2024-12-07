package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.recommendation.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    EventType recommendation_received = EventType.RECOMMENDATION_RECEIVED;

    AnalyticsEventDto toDto(AnalyticsEvent event);
    AnalyticsEvent toEntity(AnalyticsEventDto eventDto);

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    @Mapping(target = "eventType", expression = "java(map(recommendation_received))")
    AnalyticsEventDto recommendationToAnalyticsDto(RecommendationEvent recommendationEvent);

    default Integer map(EventType type) {
        return type.ordinal();
    }

    default EventType map(Integer value) {
        return EventType.of(value);
    }

}
