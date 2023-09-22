package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.dto.redis.FollowerEventDto;
import faang.school.analytics.dto.redis.PostViewEventDto;
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

    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receiverId", source = "authorId")
    AnalyticsEvent toEntity(PostViewEventDto event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorId", source = "actorId")
    @Mapping(target = "recipientId", source = "receiverId")
    @Mapping(target = "date", source = "receivedAt")
    RecommendationEventDto toRecommendationEvent(AnalyticsEvent entity);

    @Mapping(target = "createdAt", source = "receivedAt")
    @Mapping(target = "userId", source = "actorId")
    @Mapping(target = "authorId", source = "receiverId")
    PostViewEventDto toPostViewEvent(AnalyticsEvent entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actorId", source = "authorIdComment")
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "eventType", expression = "java(getEventType())")
    AnalyticsEvent toCommentEntity(CommentEventDto commentEventDto);

    @Mapping(target = "authorIdComment", source = "actorId")
    @Mapping(target = "createdAt", source = "receivedAt")
    CommentEventDto toCommentDto(AnalyticsEvent entity);


    @Mapping(target = "actorId", source = "requesterId")
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "eventType", expression = "java(getMentorshipRequestType())")
    AnalyticsEvent toMentorshipRequestedEntity(MentorshipRequestedEventDto dto);

    @Mapping(target = "receivedAt", source = "receivedAt")
    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "actorId", source = "actorId")
    AnalyticsEvent fromSearchAppearanceEventDtoToEntity(SearchAppearanceEventDto searchAppearanceEventDto);

    @Mapping(target = "receivedAt", source = "receivedAt")
    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "actorId", source = "actorId")
    SearchAppearanceEventDto toSearchAppearanceEventDto(AnalyticsEvent analyticsEvent);

    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent toEntity(FollowerEventDto event);

    @Mapping(source = "receiverId", target = "followeeId")
    @Mapping(source = "actorId", target = "followerId")
    @Mapping(source = "receivedAt", target = "timestamp")
    FollowerEventDto toDto(AnalyticsEvent entity);

    default EventType getRecommendationType() {
        return EventType.RECOMMENDATION_RECEIVED;
    }

    default EventType getEventType() {
        return EventType.POST_COMMENT;
    }

    default EventType getMentorshipRequestType() {
        return EventType.MENTORSHIP_REQUESTED;
    }
}