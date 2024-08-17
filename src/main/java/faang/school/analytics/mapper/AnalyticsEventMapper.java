package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.LikeEvent;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "authorId", target = "receiverId")
    @Mapping(source = "eventType", target = "eventType")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent likeEventToAnalyticsEvent(LikeEvent likeEvent);
}
