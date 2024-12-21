package faang.school.analytics.mapper.project_view;

import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "projectId", target = "actorId")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent toEntity(ProjectViewEvent projectViewEvent);
}