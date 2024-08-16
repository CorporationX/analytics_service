package faang.school.analytics.mapper.project;

import faang.school.analytics.dto.event.project.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectViewEventMapper {

    @Mapping(source = "viewerId", target = "actorId")
    @Mapping(source = "projectId", target = "receiverId")
    @Mapping(source = "timestamp", target = "receivedAt")
    @Mapping(source = "eventId", target = "eventId")
    AnalyticsEvent toAnalyticsEntity(ProjectViewEvent projectViewEvent);
}
