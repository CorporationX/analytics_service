package faang.school.analytics.mapper;

import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventMapper INSTANCE = Mappers.getMapper(AnalyticsEventMapper.class);

    @Mapping(source = "viewerId", target = "actorId")
    @Mapping(source = "userProfileId", target = "receiverId")
    @Mapping(source = "viewDate", target = "receivedAt")
    AnalyticsEvent fromProfileViewEventToAnalyticsEvent(ProfileViewEvent profileViewEvent);
}
