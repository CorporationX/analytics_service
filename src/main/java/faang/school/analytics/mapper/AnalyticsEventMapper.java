package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEvent toEntity(AnalyticsEventDto dto);
    AnalyticsEventDto toDto(AnalyticsEvent event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "eventType", expression = "java(mapEventType())")
    @Mapping(target = "receivedAt", source = "eventTime")
    AnalyticsEventDto toAnalyticsEventDto(FollowerEvent followerEvent);

    default EventType mapEventType() {
        return EventType.FOLLOWER;
    }

}
