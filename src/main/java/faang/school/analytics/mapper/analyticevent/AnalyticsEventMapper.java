package faang.school.analytics.mapper.analyticevent;

import faang.school.analytics.model.dto.FollowerEventDto;
import faang.school.analytics.model.dto.analyticsevent.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.event.ProfileViewEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receivedAt", source = "subscribedAt")
    AnalyticsEvent toEntity(FollowerEventDto followerEvent);

    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "actorId", source = "senderId")
    @Mapping(target = "receivedAt", source = "dateTime")
    AnalyticsEvent toEntity(ProfileViewEvent profileViewEvent);
}
