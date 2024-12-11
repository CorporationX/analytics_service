package faang.school.analytics.mapper.follower;

import faang.school.analytics.event.follower.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "eventType", constant = "FOLLOWER")
    AnalyticsEvent followerEventToAnalyticsEvent(FollowerEvent followerEvent);
}
