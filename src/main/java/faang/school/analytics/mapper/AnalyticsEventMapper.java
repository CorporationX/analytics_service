package faang.school.analytics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = {faang.school.analytics.model.EventType.class}
)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent model);
    
    AnalyticsEvent toModel(CreateAnalyticsEventDto dto);

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    @Mapping(target = "eventType", constant = "RECOMMENDATION_RECEIVED")
    CreateAnalyticsEventDto toDto(RecommendationEvent event);
}
