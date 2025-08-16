package faang.school.analytics.mapper;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.event.RecommendationReceivedEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RecommendationReceivedEventMapper {

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    @Mapping(
            target = "eventType",
            expression = "java(faang.school.analytics.model.EventType.RECOMMENDATION_RECEIVED)"
    )
    AnalyticsEventDto toAnalyticsEventDto(RecommendationReceivedEventDto recommendationReceivedEventDto);
}

