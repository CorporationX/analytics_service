package faang.school.analytics.mapper;

import faang.school.analytics.messaging.events.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(target = "receiverId", expression = "java(profileViewEvent.getIdVisited())")
    @Mapping(target = "actorId", expression = "java(profileViewEvent.getIdVisitor())")
    @Mapping(target = "eventType", ignore = true, expression = "java(EventType.of(1))")
    AnalyticsEvent viewProfileToAnalyticsEvent(ProfileViewEvent profileViewEvent);

}
