package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.dto.premium.PremiumEvent;
import faang.school.analytics.dto.premium.PremiumPeriod;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.POST_COMMENT)")
    @Mapping(target = "receivedAt", source = "createdAt")
    AnalyticsEvent toEntity(CommentEventDto commentEventDto);

    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.GOAL_COMPLETED)")
    @Mapping(target = "receivedAt", source = "goalCompletedAt")
    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "goalId")
    AnalyticsEvent toAnalyticsEvent(GoalCompletedEvent goalCompletedEvent);

    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "actorId", expression = "java(faang.school.analytics.model.EventType.FOLLOWER.ordinal())")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.FOLLOWER)")
    @Mapping(target = "receivedAt", source = "timestamp")
    AnalyticsEvent toAnalyticsEvent(FollowerEvent followerEvent);

    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "receivedAt", source = "timestamp")
    @Mapping(target = "eventType", expression = "java(getEventType(premiumEvent))")
    @Mapping(target = "actorId", expression = "java(getEventType(premiumEvent).ordinal())")
    AnalyticsEvent toAnalyticsEvent(PremiumEvent premiumEvent);

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
