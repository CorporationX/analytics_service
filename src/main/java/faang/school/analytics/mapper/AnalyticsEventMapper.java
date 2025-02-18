package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotBlank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "timestamp", target = "receivedAt")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "followerId", target = "actorId")
    @Mapping(constant  = "FOLLOWER", target = "eventType")
    AnalyticsEvent toAnalyticsEventEntity(FollowerEventDto followerEventDto);

    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toAnalyticsEventEntity(AnalyticsEventDto analyticsEventDto);

}
