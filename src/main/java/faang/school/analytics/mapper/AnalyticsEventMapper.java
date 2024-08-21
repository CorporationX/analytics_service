package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto analyticsEventToAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    List<AnalyticsEventDto> analyticsEventListToAnalyticsEventDtoList(List<AnalyticsEvent> analyticsEventList);

    @Mapping(target = "eventType", constant = "PROJECT_VIEW")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "receivedAt", target = "receivedAt")
    AnalyticsEvent projectViewEventToAnalyticsEvent(ProjectViewEvent projectViewEvent);
}
