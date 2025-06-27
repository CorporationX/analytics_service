package faang.school.analytics;

import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AnalyticsEventMapper {
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "receivedAt", expression = "java(EventType.FOLLOWER")
    @Mapping(target = "receivedAt", source = "subscriptionTime")
    AnalyticsEvent toAnalyticEvent(FollowerEvent followerEvent);
}
