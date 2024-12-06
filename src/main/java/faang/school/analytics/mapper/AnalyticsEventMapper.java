package faang.school.analytics.mapper;

import faang.school.analytics.message.event.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "eventType", constant = "PROFILE_VIEW")
    AnalyticsEvent toAnalyticsEvent(ProfileViewEvent profileViewEvent);
}
