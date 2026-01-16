package faang.school.analytics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = EventType.class
)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent model);
    
    AnalyticsEvent toModel(CreateAnalyticsEventDto dto);

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    @Mapping(target = "eventType", constant = "RECOMMENDATION_RECEIVED")
    CreateAnalyticsEventDto toDto(RecommendationEvent event);

    @Mapping(source = "commentAuthorId", target = "actorId")
    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "createdAt", target = "receivedAt")
    @Mapping(target = "eventType", constant = "POST_COMMENT")
    CreateAnalyticsEventDto toDto(CommentEvent event);
}
