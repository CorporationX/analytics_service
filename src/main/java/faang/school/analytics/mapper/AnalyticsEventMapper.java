package faang.school.analytics.mapper;

import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.event.RecommendationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(target = "actorId", source = "searchUserId")
    @Mapping(target = "receiverId",source = "userId")
    @Mapping(target = "receivedAt",source = "dateAndTimeViewing")
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toAnalyticEvent(SearchAppearanceEvent searchAppearanceEvent);

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toAnalyticEvent(RecommendationEvent event);

    @Mapping(target = "receiverId", source = "viewedUserId")
    @Mapping(target = "actorId", source = "viewingUserId")
    @Mapping(target = "receivedAt", source = "viewedAt")
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toAnalyticEvent(ProfileViewEvent event);

}
