package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.redis.event.LikeEvent;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(source = "likeEvent.idAuthor", target = "receiverId")
    @Mapping(source = "likeEvent.idUser", target = "actorId")
    @Mapping(source = "likeEvent.dateTime", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(LikeEvent likeEvent);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "eventType", expression = "java(getPostCommentType())")
    AnalyticsEvent toCommentEntity(CommentEventDto commentEventDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actorId", source = "searcherId")
    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "receivedAt", source = "date")
    @Mapping(target = "eventType", expression = "java(getSearchAppearanceType())")
    AnalyticsEvent toSearchAppearanceEntity(SearchAppearanceEventDto searchAppearanceEventDto);

    @Mapping(target = "authorId", source = "actorId")
    @Mapping(target = "createdAt", source = "receivedAt")
    CommentEventDto toCommentDto(AnalyticsEvent entity);

    @AfterMapping
    default void setLikeType(@MappingTarget AnalyticsEvent analyticsEvent) {
        analyticsEvent.setEventType(EventType.POST_LIKE);
    }

    default EventType getPostCommentType() {
        return EventType.POST_COMMENT;
    }

    default EventType getSearchAppearanceType() {
        return EventType.PROFILE_APPEARED_IN_SEARCH;
    }

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
}