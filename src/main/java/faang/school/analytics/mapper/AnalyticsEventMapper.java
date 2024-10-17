package faang.school.analytics.mapper;

import faang.school.analytics.model.FollowerEvent;
import faang.school.analytics.model.dto.SearchAppearanceEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEvent fromSearchAppearanceToEntity(SearchAppearanceEvent searchAppearanceEvent);

    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receiverId", source = "followedUserId")
    @Mapping(target = "eventType", constant = "FOLLOWER")
    @Mapping(target = "receivedAt", source = "subscriptionTime")
    AnalyticsEvent toEntity(FollowerEvent event);

    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "eventType", constant = "PREMIUM_PURCHASE")
    @Mapping(target = "receivedAt", source = "purchaseDateTime")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "subscriptionDuration", source = "subscriptionDuration")
    AnalyticsEvent fromPremiumBoughtEventToEntity(PremiumBoughtEventDto event);
}
}