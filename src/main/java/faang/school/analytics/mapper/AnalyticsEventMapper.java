package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "timestamp", target = "receivedAt")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "followerId", target = "actorId")
    @Mapping(constant  = "FOLLOWER", target = "eventType")
    AnalyticsEvent toAnalyticsEventEntity(FollowerEventDto followerEventDto);

    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toAnalyticsEventEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "commentAuthorId", target = "actorId")
    @Mapping(source = "commentedAt", target = "receivedAt")
    @Mapping(constant  = "POST_COMMENT", target = "eventType")
    AnalyticsEvent toAnalyticsEventEntity(CommentEvent commentEvent);

}
