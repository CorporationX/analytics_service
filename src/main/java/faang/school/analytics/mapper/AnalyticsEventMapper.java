package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.CommentEventDto;
import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.event.MentorshipRequestedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(target = "eventType", constant = "POST_LIKE")
    @Mapping(source = "authorId", target = "authorId")
    @Mapping(source = "likedByUserId", target = "receiverId")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent toLikeEntity(LikeEvent event);

    @Mapping(target = "eventType", constant = "MENTORSHIP_REQUESTED")
    @Mapping(source = "MentorshipRequestSenderId", target = "authorId")
    @Mapping(source = "MentorId", target = "receiverId")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent toMentorshipEntity(MentorshipRequestedEvent event);

    AnalyticsEvent toAnalyticsEvent(CommentEventDto commentEventDto);
}