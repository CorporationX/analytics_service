package faang.school.analytics.mapper;

import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring",unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "subscriptionTime", target = "receivedAt")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "followerId", target = "actorId")
    AnalyticsEvent toEntity(FollowerEventDto followerEventDto);
}
