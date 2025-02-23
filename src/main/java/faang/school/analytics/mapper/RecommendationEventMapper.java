package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.RecommendationEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecommendationEventMapper {
    @Mapping(source = "requesterId", target = "actorId")
    @Mapping(source = "createdAt",   target = "receivedAt")
    AnalyticsEventDto toAnalyticsEvent(RecommendationEventDto recommendationEvent);

    RecommendationEventDto toRecommendationEvent(AnalyticsEventDto analyticsEvent);
}
