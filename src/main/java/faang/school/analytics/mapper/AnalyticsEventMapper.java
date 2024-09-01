package faang.school.analytics.mapper;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
  
    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "subscriptionTime", target = "receivedAt")
    @Mapping(target = "eventType", constant = "FOLLOWER")
    AnalyticsEvent toEntity(FollowerEvent dto);
  
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

}
