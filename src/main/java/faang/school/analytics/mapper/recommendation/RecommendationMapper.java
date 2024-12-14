package faang.school.analytics.mapper.recommendation;

import faang.school.analytics.dto.recommendation.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecommendationMapper {

    AnalyticsEvent toAnalyticsEvent(RecommendationEvent recommendationEvent);

    RecommendationEvent toRecommendationEvent(AnalyticsEvent analyticsEvent);
}
