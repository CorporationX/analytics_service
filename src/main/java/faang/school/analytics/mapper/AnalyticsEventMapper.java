package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.events.PostLikeEvent;
import faang.school.analytics.events.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.RecommendationEvent;
import faang.school.analytics.model.CommentEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    AnalyticsEventDto toDto(PostViewEvent postViewEvent);

    @Mapping(source = "commentId", target = "receiverId")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "sendAt", target = "receivedAt")
    AnalyticsEvent commentEventToAnalyticsEvent(CommentEvent commentEvent);

    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "actorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(PostLikeEvent postLikeEvent);

    AnalyticsEvent toAnalyticsEvent(RecommendationEvent recommendationEvent);
}