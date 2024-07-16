package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "eventType", ignore = true)
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "receiverId", source = "postId")
    AnalyticsEvent toCommentEntity(CommentEventDto commentEventDto);

    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "viewerId", target = "actorId")
    @Mapping(target = "eventType", ignore = true)
    @Mapping(source = "viewTime", target = "receivedAt")
    AnalyticsEvent toEntity(PostViewEvent postViewEvent);

    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "likeUserId", target = "actorId")
    AnalyticsEvent toEntity(LikeEvent event);

    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(target = "eventType", ignore = true)
    @Mapping(source = "subscriptionDate", target = "receivedAt")
    AnalyticsEvent toFollowerEntity(FollowerEvent followerEvent);
}
