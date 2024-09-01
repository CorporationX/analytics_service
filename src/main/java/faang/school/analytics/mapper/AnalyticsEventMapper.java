package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.publishable.MentorshipRequestedEvent;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "subscriptionTime", target = "receivedAt")
    @Mapping(target = "eventType", constant = "FOLLOWER")
    AnalyticsEvent toEntity(FollowerEvent dto);

    @Mapping(source = "requesterId", target = "actorId")
    @Mapping(source = "timestamp", target = "receivedAt")
    @Mapping(target = "eventType", constant = "MENTORSHIP_REQUEST")
    AnalyticsEvent toEntity(MentorshipRequestedEvent dto);
}
