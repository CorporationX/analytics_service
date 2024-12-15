package faang.school.analytics.mapper.events;

import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.recommendation.RecommendationEvent;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    EventType recommendation_received = EventType.RECOMMENDATION_RECEIVED;
    EventType comment_received = EventType.POST_COMMENT;

    @Mapping(source = "eventTypeNumber", target = "eventType", qualifiedByName = "mapToEventType")
    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "eventType", target = "eventTypeNumber", qualifiedByName = "mapToEventTypeNumber")
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    @Mapping(target = "eventTypeNumber", expression = "java(map(recommendation_received))")
    AnalyticsEventDto recommendationToAnalyticsDto(RecommendationEvent recommendationEvent);

    @Mapping(source = "commentId", target = "receiverId")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "date", target = "receivedAt")
    @Mapping(target = "eventTypeNumber", expression = "java(map(comment_received))")
    AnalyticsEventDto commentToAnalyticsDto(CommentEvent commentEvent);

    @Named("mapToEventTypeNumber")
    default int map(EventType eventType) {
        return eventType.ordinal();
    }

    @Named("mapToEventType")
    default EventType map(int eventTypeNumber) {
        return EventType.of(eventTypeNumber);
    }
}
