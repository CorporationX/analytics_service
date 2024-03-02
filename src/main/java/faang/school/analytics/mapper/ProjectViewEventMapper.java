package faang.school.analytics.mapper;

import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectViewEventMapper extends AnalyticsEventMapper<ProjectViewEvent> {
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.PROJECT_VIEW)")
    @Mapping(target = "receiverId", source = "projectId")
    @Mapping(target = "actorId", source = "ownerId")
    AnalyticsEvent toAnalyticsEvent(ProjectViewEvent projectViewEvent);

    @Override
    default Class<ProjectViewEvent> getType() {
        return ProjectViewEvent.class;
    }
}