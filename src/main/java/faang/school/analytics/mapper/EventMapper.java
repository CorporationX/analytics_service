package faang.school.analytics.mapper;

import faang.school.analytics.dto.event_broker.FollowerEvent;
import faang.school.analytics.dto.event_broker.PremiumEvent;
import faang.school.analytics.dto.premium.PremiumPeriod;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = "receiverId", source = "userId"),
            @Mapping(target = "receivedAt", source = "timestamp"),
            @Mapping(target = "eventType", expression = "java(getEventType(premiumEvent).name())"),
            @Mapping(target = "actorId", source = "premiumId")
    })
    AnalyticsEvent toAnalyticsEvent(PremiumEvent premiumEvent);

    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = "receiverId", source = "followeeId"),
            @Mapping(target = "actorId", source = "followerId"),
            @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.FOLLOWER.name())"),
            @Mapping(target = "receivedAt", source = "timestamp")
    })
    AnalyticsEvent toAnalyticsEvent(FollowerEvent followerEvent);

    default EventType getEventType(PremiumEvent premiumEvent) {
        if (premiumEvent.getPremiumPeriod() == PremiumPeriod.ONE_MONTH) {
            return EventType.PREMIUM_ONE_MONTH;
        } else if (premiumEvent.getPremiumPeriod() == PremiumPeriod.THREE_MONTHS) {
            return EventType.PREMIUM_THREE_MONTHS;
        } else if (premiumEvent.getPremiumPeriod() == PremiumPeriod.ONE_YEAR) {
            return EventType.PREMIUM_ONE_YEAR;
        } else {
            throw new DataValidationException("Такого периода нет: " + premiumEvent.getPremiumPeriod());
        }
    }
}
