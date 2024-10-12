package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.MentorshipRequestEvent;
import faang.school.analytics.event.ProjectViewEvent;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticMapper {
    @Mapping(source = "requesterId", target = "actorId")
    @Mapping(source = "requestTime", target = "receivedAt")
    AnalyticsEvent toAnalytic(MentorshipRequestEvent event);

    @Mapping(target = "receiverId", source = "projectId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "viewTime")
    AnalyticsEvent toEntity(ProjectViewEvent projectViewEvent);
}
