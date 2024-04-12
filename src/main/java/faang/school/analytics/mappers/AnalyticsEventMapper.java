package faang.school.analytics.mappers;

import faang.school.analytics.dto.ProfileViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "observerId", target = "actorId")
    @Mapping(source = "observedId", target = "receiverId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    @Mapping(target = "eventType", constant = "PROFILE_VIEW")
    AnalyticsEvent toAnalyticsEvent(ProfileViewEventDto profileViewEventDto);

}
