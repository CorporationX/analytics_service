package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent event);

    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receiverId", expression = "java(toReceiverId(followerEvent))")
    @Mapping(target = "eventType", constant = "FOLLOWER")
    @Mapping(target = "receivedAt", source = "timestamp")
    AnalyticsEventDto toDto(FollowerEvent followerEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto eventDto);

    default Long toReceiverId(FollowerEvent event) {
        return event.followeeId() != null ? event.followeeId() : event.projectId();
    }
}
