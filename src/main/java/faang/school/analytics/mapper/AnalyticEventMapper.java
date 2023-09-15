package faang.school.analytics.mapper;

import faang.school.analytics.messaging.event.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticEventMapper {
    @Mapping(source = "profileViewEvent.userId", target = "actorId")
    @Mapping(source = "profileViewEvent.profileViewedId", target = "receiverId")
    @Mapping(source = "profileViewEvent.date", target = "receivedAt")
    AnalyticsEvent profileViewToAnalyticsEvent(ProfileViewEvent profileViewEvent);
}
