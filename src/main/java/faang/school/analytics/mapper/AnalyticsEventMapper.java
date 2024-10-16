package faang.school.analytics.mapper;

import faang.school.analytics.model.dto.*;
import faang.school.analytics.model.entity.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto analyticEventDto);
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
    AnalyticsEvent fromSearchAppearanceToEntity(SearchAppearanceEvent searchAppearanceEvent);

    AnalyticsEvent fromProfileViewToEntity(ProfileViewEvent profileViewEvent);

    @Mapping(source = "userId", target = "actorId")
    AnalyticsEvent fromAdBoughtToEntity(AdBoughtEvent adBoughtEvent);

    @Mapping(source = "postId", target = "receiverId")
    AnalyticsEvent fromPostViewToEntity(PostViewEvent postViewEvent);
}