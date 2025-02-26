package faang.school.analytics.mapper;

import faang.school.analytics.dto.recommendationevent.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "eventType", constant = "RECOMMENDATION_RECEIVED")
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "id", ignore = true)
    AnalyticsEvent mapRecommendationToAnalyticsEvent(RecommendationEvent event);
}
