package faang.school.analytics.mapper;

import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileViewEventMapper {
    @Mapping(source = "viewerId", target = "actorId")
    @Mapping(source = "viewedId", target = "receiverId")
    AnalyticsEvent toAnalyticsEvent(ProfileViewEvent profileViewEvent);
}
