package faang.school.analytics.mapper;

import faang.school.analytics.dto.AdBoughtEventDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.listener.projectview.ProjectViewEvent;
import faang.school.analytics.dto.FundRaisedEvent;
import faang.school.analytics.dto.premium.BoughtPremiumEventDto;
import faang.school.analytics.dto.recommendation.RecommendationEventDto;
import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import faang.school.analytics.dto.profileView.ProfileViewEvent;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = EventType.class)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "receivedAt")
    AnalyticsEvent toEntity(BoughtPremiumEventDto eventDto);

    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "actorId", source = "authorId")
    AnalyticsEvent toEntity(RecommendationEventDto eventDto);

    @Mapping(source = "createdAt", target = "receivedAt")
    @Mapping(source = "projectId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    AnalyticsEvent fromProjectViewToAnalyticsEvent(ProjectViewEvent event);

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "amount", target = "receiverId")
    @Mapping(source = "donationTime", target = "receivedAt")
    AnalyticsEvent toEntity(FundRaisedEvent analyticsEventDto);

    @Mapping(target = "receiverId", source = "foundUserId")
    @Mapping(target = "actorId", source = "requesterId")
    @Mapping(target = "eventType", expression = "java(EventType.PROFILE_APPEARED_IN_SEARCH)")
    AnalyticsEvent toEntity(SearchAppearanceEventDto searchAppearanceEventDto);

    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "boughtAt")
    AnalyticsEvent toEntity(AdBoughtEventDto adBoughtEventDto);

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "userIdViewing", target = "receiverId")
    @Mapping(source = "dateTimeOfViewing", target = "receivedAt")
    AnalyticsEvent toEntity(ProfileViewEvent analyticsEventDto);
}