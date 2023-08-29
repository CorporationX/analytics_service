package faang.school.analytics.mapper;

import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "receiverId", source = "recipientId")
    @Mapping(target = "receivedAt", source = "date")
    @Mapping(target = "eventType", expression = "java(getRecommendationType())")
    AnalyticsEvent toEntity(RecommendationEventDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorId", source = "actorId")
    @Mapping(target = "recipientId", source = "receiverId")
    @Mapping(target = "date", source = "receivedAt")
    RecommendationEventDto toDto(AnalyticsEvent entity);

    default EventType getRecommendationType() {
        return EventType.of(8);
    }
}
