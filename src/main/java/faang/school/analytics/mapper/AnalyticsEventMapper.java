package faang.school.analytics.mapper;

import faang.school.analytics.dtoForRedis.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Objects;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "visitorId", target = "actorId")
    @Mapping(source = "followerEventDto", target = "receiverId", qualifiedByName = "getReceiverId")
    @Mapping(source = "subscribedDateTime", target = "receivedAt")
    @Mapping(source = "followerEventDto", target = "eventType", qualifiedByName = "getEventType")
    AnalyticsEvent toAnalyticEvent(FollowerEventDto followerEventDto);

    @Named("getEventType")
    default EventType getEventType(FollowerEventDto followerEventDto) {
        return Objects.isNull(followerEventDto.getVisitedId()) ?
                EventType.PROJECT_FOLLOWER : EventType.USER_FOLLOWER;
    }

    @Named("getReceiverId")
    default Long getReceiverId(FollowerEventDto followerEventDto) {
        if (Objects.nonNull(followerEventDto.getProjectId())) {
            return followerEventDto.getProjectId();
        } else {
            return followerEventDto.getVisitedId();
        }
    }
}
