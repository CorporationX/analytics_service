package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent event);


    @Mapping(target = "receiverId", source = "projectId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "timestamp")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.PROJECT_VIEW)")
    AnalyticsEvent toAnalyticsProjectEvent(ProjectViewEvent projectViewEvent);
}
