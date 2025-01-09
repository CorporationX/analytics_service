package faang.school.analytics.mapper.following;

import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.following.FollowerEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = EventType.class)
public interface FollowerEventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receiverId", source = "event", qualifiedByName = "receiverId")
    @Mapping(target = "actorId", source = "followerUserId")
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "eventTypeNumber", expression = "java(map(EventType.FOLLOWER))")
    AnalyticsEventDto toCommonEvent(FollowerEvent event);

    @Named("receiverId")
    default long map(FollowerEvent event) {
        if (event.getTargetUserId() != null) {
            return event.getTargetUserId();
        }
        return event.getTargetProjectId();
    }

    @Named("mapToEventTypeNumber")
    default int map(EventType eventType) {
        return eventType.ordinal();
    }
}
