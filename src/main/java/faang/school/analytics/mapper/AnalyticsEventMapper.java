package faang.school.analytics.mapper;

import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(target = "receiverId", source = "foundUserId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "eventType", constant = "PROFILE_APPEARED_IN_SEARCH")
    @Mapping(target = "receivedAt", source = "time")
    @Mapping(target = "id", ignore = true)
    AnalyticsEvent mapSearchAppearanceToAnalyticEvent(SearchAppearanceEvent event);
}
