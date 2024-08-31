package faang.school.analytics.mapper;

import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowerEventMapper {

    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "eventTime", target = "receivedAt")
    @Mapping(constant = "FOLLOWER", target = "eventType")
    AnalyticsEvent toEntity(FollowerEventDto followerEventDto);
}
