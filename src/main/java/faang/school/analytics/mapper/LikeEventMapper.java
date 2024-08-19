package faang.school.analytics.mapper;

import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeEventMapper {
    LikeEvent toDto(AnalyticsEvent analyticsEvent);

    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "eventType", target = "eventType")
    @Mapping(source = "receivedAt", target = "receivedAt")
    AnalyticsEvent toEntity(LikeEvent likeEvent);

}
