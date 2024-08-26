package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.MentorshipRequestEvent;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "eventType", constant = "MENTORSHIP_REQUESTED")
    @Mapping(target = "actorId", source = "requesterId")
    @Mapping(target = "receivedAt", source = "requestedAt")
    AnalyticsEvent toAnalyticsEvent(MentorshipRequestEvent event);

    @Mapping(source = "id", target = "postId")
    @Mapping(source = "receiverId", target = "userId")
    @Mapping(source = "actorId", target = "authorLikeId")
    @Mapping(source = "receivedAt", target = "localDateTime")
    LikeEvent toLikeEvent(AnalyticsEvent analyticsEvent);

    @Mapping(source = "postId", target = "id")
    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "authorLikeId", target = "actorId")
    @Mapping(target = "eventType", constant = "POST_LIKE")
    @Mapping(source = "localDateTime", target = "receivedAt")
    AnalyticsEvent toAnalyticsEventFromLikeEvent(LikeEvent likeEvent);
}