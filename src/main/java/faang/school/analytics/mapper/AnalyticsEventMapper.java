package faang.school.analytics.mapper;

import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(FollowerEvent followerEvent);
}
