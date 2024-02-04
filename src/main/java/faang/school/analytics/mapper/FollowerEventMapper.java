package faang.school.analytics.mapper;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowerEventMapper {
    @Mappings({
        @Mapping(target = "receiverId", source = "followeeId"),
        @Mapping(target = "actorId", expression = "java(faang.school.analytics.model.EventType.FOLLOWER.ordinal())"),
        @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.FOLLOWER)"),
        @Mapping(target = "receivedAt", source = "timestamp")
    })
    AnalyticsEvent toAnalyticsEvent(FollowerEvent followerEvent);
}
