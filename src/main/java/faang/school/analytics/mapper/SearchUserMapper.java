package faang.school.analytics.mapper;

import faang.school.analytics.dto.messagebroker.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SearchUserMapper {
    @Mapping(target = "actorId", source = "searchUserId")
    @Mapping(target = "receiverId",source = "userId")
    @Mapping(target = "receivedAt",source = "dateAndTimeViewing")
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toAnalyticEvent(SearchAppearanceEvent searchAppearanceEvent);

    default void setEventType(AnalyticsEvent analyticsEvent){
        analyticsEvent.setEventType(EventType.PROFILE_APPEARED_IN_SEARCH);
    }
}
