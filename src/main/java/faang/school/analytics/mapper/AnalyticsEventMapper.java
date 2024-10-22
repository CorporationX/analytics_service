package faang.school.analytics.mapper;

import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.dto.AdBoughtEvent;
import faang.school.analytics.model.dto.FundRaisedEvent;
import faang.school.analytics.model.dto.ProfileViewEvent;
import faang.school.analytics.model.dto.SearchAppearanceEvent;
import faang.school.analytics.model.FollowerEvent;
import faang.school.analytics.model.dto.*;
import faang.school.analytics.model.entity.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto analyticEventDto);


    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followedUserId", target = "receiverId")
    @Mapping(source = "subscriptionTime", target = "receivedAt")
    @Mapping(target = "eventType", constant = "FOLLOWER_EVENT")
    @Mapping(target = "id", ignore = true)

    AnalyticsEvent fromFollowerEventToEntity(FollowerEvent followerEvent);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent fromSearchAppearanceToEntity(SearchAppearanceEvent searchAppearanceEvent);

    AnalyticsEvent fromProfileViewToEntity(ProfileViewEvent profileViewEvent);

    AnalyticsEvent fromAdBoughtToEntity(AdBoughtEvent adBoughtEvent);

    @Mapping(source = "projectId", target = "receiverId")
    AnalyticsEvent fromFundRaisedToEntity(FundRaisedEvent fundRaisedEvent);

    @Mapping(source = "subscriptionDuration.days", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    AnalyticsEvent fromPremiumBoughtToEntity(PremiumBoughtEventDto premiumBoughtEventDto);

}