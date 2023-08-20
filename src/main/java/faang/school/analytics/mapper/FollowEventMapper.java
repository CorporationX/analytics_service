package faang.school.analytics.mapper;

import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowEventMapper {

    @Mapping(source = "subscriberId", target = "actorId")
    @Mapping(source = "targetUserId", target = "receiverId")
    @Mapping(source = "subscriptionDateTime", target = "receivedAt")
    AnalyticsEvent toEntity(FollowEventDto dto);

    FollowEventDto toDto(AnalyticsEvent dto);
}