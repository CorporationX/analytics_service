package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.RecommendationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEvent toEntity(AnalyticsEventDto dto);
    AnalyticsEventDto toDto(AnalyticsEvent event);

    @Mapping(target = "id", source = "recommendationId")
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.RECOMMENDATION_RECEIVED)")
    AnalyticsEventDto fromRecommendationEventToDto(RecommendationEvent recommendationEvent);

}
