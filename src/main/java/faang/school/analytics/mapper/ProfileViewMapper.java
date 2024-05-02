package faang.school.analytics.mapper;

import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProfileViewMapper extends AnalyticsMapper<ProfileViewEvent, AnalyticsEvent>{
    @Override
    @Mapping(target = "receiverId", source = "viewedUserId")
    @Mapping(target = "actorId", source = "viewingUserId")
    @Mapping(target = "receivedAt", source = "viewedAt")
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toAnalyticsEvent(ProfileViewEvent event);
}
