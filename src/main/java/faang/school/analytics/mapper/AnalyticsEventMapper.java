package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent entity);

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "projectId", target = "actorId")
    @Mapping(source = "eventTime", target = "receivedAt")
    @Mapping(constant = "PROJECT_VIEW", target = "eventType")
    AnalyticsEvent projectViewEventToAnalyticsEvent(ProjectViewEvent projectViewEvent);
}
