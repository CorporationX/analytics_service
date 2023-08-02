package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.redis.events.ProjectEvent;
import faang.school.analytics.service.redis.events.UserEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectEventsMapper {
    AnalyticsEvent toAnalyticsEvent(ProjectEvent projectEvent);
}
