package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.ProfileViewEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileViewEventMapper {

    @Mapping(source = "viewerId",target = "actorId")
    @Mapping(source = "profileId",target = "receiverId")
    @Mapping(source = "viewedAt",target = "receivedAt")
    @Mapping(target = "eventType", constant = "PROFILE_VIEW")
    AnalyticsEventDto toAnalyticsDto(ProfileViewEvent profileViewEvent);
}
