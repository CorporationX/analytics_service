package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.dto.LikeEventDto;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.event.ProjectViewProfileEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "timestamp", target = "receivedAt")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "followerId", target = "actorId")
    @Mapping(constant  = "FOLLOWER", target = "eventType")
    AnalyticsEvent toAnalyticsEventEntity(FollowerEventDto followerEventDto);

    @Mapping(source = "projectId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "dateTime", target = "receivedAt")
    @Mapping(constant  = "PROJECT_VIEW", target = "eventType")
    AnalyticsEvent toAnalyticsEventEntity(ProjectViewProfileEvent projectViewProfileEvent);

    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toAnalyticsEventEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "commentAuthorId", target = "actorId")
    @Mapping(source = "commentedAt", target = "receivedAt")
    @Mapping(constant  = "POST_COMMENT", target = "eventType")
    AnalyticsEvent toAnalyticsEventEntity(CommentEvent commentEvent);

    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "postId", target = "postId")
    @Mapping(constant = "POST_LIKE", target = "eventType")
    @Mapping(target = "receivedAt", source = "timestamp", qualifiedByName = "mapTimestampToLocalDateTime")
    AnalyticsEvent toLikeEventEntity(LikeEventDto likeEventDto);

    @Named("mapTimestampToLocalDateTime")
    default LocalDateTime mapTimestampToLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
