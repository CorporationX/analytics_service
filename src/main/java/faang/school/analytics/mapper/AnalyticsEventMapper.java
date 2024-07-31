package faang.school.analytics.mapper;

import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.dto.event.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followingDate", target = "receivedAt")
    AnalyticsEventDto fromFollowerEventToDto(FollowerEvent event);

    @Mapping(source = "commentAuthorId", target = "actorId")
    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "postId", target = "postId")
    @Mapping(target = "eventType", constant = "POST_COMMENT")
    @Mapping(source = "commentId", target = "commentId")
    @Mapping(target = "receivedAt", expression = "java(java.time.LocalDateTime.now())")
    AnalyticsEventDto fromCommentEventToDto(CommentEvent event);


}