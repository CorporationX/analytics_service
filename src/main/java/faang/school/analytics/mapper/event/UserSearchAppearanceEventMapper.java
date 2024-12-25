package faang.school.analytics.mapper.event;

import faang.school.analytics.event.UserSearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserSearchAppearanceEventMapper {
    @Mapping(source = "searchedUserId", target = "receiverId")
    @Mapping(source = "userWhoSearchId", target = "actorId")
    @Mapping(source = "searchTime", target = "receivedAt")
    AnalyticsEvent toEntity(UserSearchAppearanceEvent goalCompletedEvent);
}