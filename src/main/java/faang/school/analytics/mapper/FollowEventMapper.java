package faang.school.analytics.mapper;

import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowEventMapper {
    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "targetId",   target = "receiverId")
    @Mapping(source = "timestamp",  target = "receivedAt")
    @Mapping(target = "eventType", constant = "FOLLOW")
    AnalyticsEvent toEntity(FollowerEvent followerEvent);
}
