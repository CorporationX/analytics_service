package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.event.UserPremiumBoughtEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    EventType PREMIUM_BOUGHT = EventType.PREMIUM_BOUGHT;
    EventType POST_VIEW = EventType.POST_VIEW;
    EventType PROJECT_VIEW = EventType.PROJECT_VIEW;

    List<AnalyticsEventDto> toDto(List<AnalyticsEvent> analyticsEvents);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "eventType", expression = "java(AnalyticsEventMapper.PREMIUM_BOUGHT)")
    AnalyticsEvent toEntity(UserPremiumBoughtEvent userPremiumBoughtEvent);

    @Mapping(source = "authorId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    @Mapping(target = "eventType", expression = "java(AnalyticsEventMapper.POST_VIEW)")
    AnalyticsEvent toAnalyticsPostEvent(PostViewEvent postViewEvent);

    @Mapping(target = "receiverId", source = "projectId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "timestamp")
    @Mapping(target = "eventType", expression = "java(AnalyticsEventMapper.PROJECT_VIEW)")
    AnalyticsEvent toAnalyticsProjectEvent(ProjectViewEvent projectViewEvent);
}
