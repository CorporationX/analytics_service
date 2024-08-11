package faang.school.analytics.mapper;

import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowerEventMapper extends EventMapper {

    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "eventTime", target = "receivedAt")
    @Mapping(source = "eventType", target = "eventType", qualifiedByName = "followerType")
    AnalyticsEvent toEntity(FollowerEventDto followerEventDto);

    @Named("followerType")
    default EventType followerType(String type) {
        return EventType.FOLLOWER;
    }
}
