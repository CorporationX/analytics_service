package faang.school.analytics.mapper.event.profile;

import faang.school.analytics.event.profile.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "viewerId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(ProfileViewEvent profileViewEvent);
}