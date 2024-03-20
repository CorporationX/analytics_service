package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.dto.PremiumBoughtEventDto;
import faang.school.analytics.dto.ProjectViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "actorId", expression = "java(faang.school.analytics.model.EventType.PREMIUM_BOUGHT.ordinal())")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.PREMIUM_BOUGHT)")
    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEventDto event);

    @Mapping(source = "requesterId", target = "actorId")
    AnalyticsEvent toAnalyticsEvent(MentorshipRequestedEventDto event);

    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receiverId", source = "followeeId")
    AnalyticsEvent toAnalyticsEvent(FollowerEventDto followerEventDto);

    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.PROJECT_VIEW)")
    @Mapping(target = "receiverId", source = "projectId")
    @Mapping(target = "actorId", source = "ownerId")
    AnalyticsEvent toAnalyticsEvent(ProjectViewEventDto projectViewEventDto);

    AnalyticsEventDto toDto (AnalyticsEvent analyticsEvent);
}
