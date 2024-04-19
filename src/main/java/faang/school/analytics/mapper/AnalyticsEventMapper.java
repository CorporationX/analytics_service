package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEvent;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    EventType PROJECT_VIEW = EventType.PROJECT_VIEW;
    EventType POST_VIEW = EventType.POST_VIEW;

    faang.school.analytics.model.AnalyticsEvent toEntity(AnalyticsEvent analyticsEventDto);

    AnalyticsEvent toDto(faang.school.analytics.model.AnalyticsEvent analyticsEvent);

    @Mapping(target = "receiverId", source = "projectId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "timestamp")
    @Mapping(target = "eventType", expression = "java(AnalyticsEventMapper.PROJECT_VIEW)")
    faang.school.analytics.model.AnalyticsEvent toAnalyticsEvent(ProjectViewEvent projectViewEvent);

    @Mapping(source = "authorId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    @Mapping(target = "eventType", expression = "java(AnalyticsEventMapper.POST_VIEW)")
    AnalyticsEvent toAnalyticsEvent(PostViewEvent postViewEvent);
}
