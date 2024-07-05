package faang.school.analytics.mapper;

import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeEventMapper {

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "completedAt", target = "receivedAt")
    @Mapping(source = "postId", target = "receiverId")
    @Mapping(target = "eventType", constant = "POST_LIKE")
    AnalyticsEvent toAnalyticsEvent(LikeEvent likeEvent);
}
