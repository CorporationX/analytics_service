package faang.school.analytics.mapper;

import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RecommendationEventMapper {
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toAnalyticEvent(RecommendationEvent event);

    default void setEventType(AnalyticsEvent analyticsEvent) {
        analyticsEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);
    }
}
