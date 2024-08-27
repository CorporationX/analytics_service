package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dtoForRedis.FollowerEventDto;
import faang.school.analytics.event.MentorshipRequestEvent;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Objects;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "visitorId", target = "actorId")
    @Mapping(source = "followerEventDto", target = "receiverId", qualifiedByName = "getReceiverId")
    @Mapping(source = "subscribedDateTime", target = "receivedAt")
    @Mapping(source = "followerEventDto", target = "eventType", qualifiedByName = "getEventType")
    AnalyticsEvent toAnalyticEvent(FollowerEventDto followerEventDto);

    @Mapping(target = "eventType", constant = "MENTORSHIP_REQUESTED")
    @Mapping(target = "actorId", source = "requesterId")
    @Mapping(target = "receivedAt", source = "requestedAt")
    AnalyticsEvent toAnalyticsEvent(MentorshipRequestEvent event);

    AnalyticsEvent toEntity(AnalyticEventDto analyticEventDto);
    AnalyticEventDto toDto(AnalyticsEvent analyticsEvent);

    @Named("getEventType")
    default EventType getEventType(FollowerEventDto followerEventDto) {
        return Objects.isNull(followerEventDto.getVisitedId()) ?
                EventType.PROJECT_FOLLOWER : EventType.USER_FOLLOWER;
    }

    @Named("getReceiverId")
    default Long getReceiverId(FollowerEventDto followerEventDto) {
        if (Objects.nonNull(followerEventDto.getProjectId())) {
            return followerEventDto.getProjectId();
        } else {
            return followerEventDto.getVisitedId();
        }
    }

    @Mapping(source = "id", target = "postId")
    @Mapping(source = "receiverId", target = "userId")
    @Mapping(source = "actorId", target = "authorPostId")
    @Mapping(source = "receivedAt", target = "localDateTime")
    LikeEvent toLikeEvent(AnalyticsEvent analyticsEvent);

    @Mapping(source = "postId", target = "id")
    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "authorPostId", target = "actorId")
    @Mapping(target = "eventType", constant = "POST_LIKE")
    @Mapping(source = "localDateTime", target = "receivedAt")
    AnalyticsEvent toAnalyticsEventFromLikeEvent(LikeEvent likeEvent);

//    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);
//
//    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
}
