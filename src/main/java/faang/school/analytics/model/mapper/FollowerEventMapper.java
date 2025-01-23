package faang.school.analytics.model.mapper;

import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.dto.FollowerEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowerEventMapper {
    @Mapping(target = "eventType", constant = "FOLLOWER")
    @Mapping(source = "followerId", target = "receiverId")
    @Mapping(source = "followeeId", target = "actorId")
    AnalyticsEventDto toAnalyticsEventDto(FollowerEvent followerEvent);

    @Mapping(source = "receiverId", target = "followerId")
    @Mapping(source = "actorId", target = "followeeId")
    FollowerEvent toFollowerEvent(AnalyticsEventDto analyticsEventDto);
}