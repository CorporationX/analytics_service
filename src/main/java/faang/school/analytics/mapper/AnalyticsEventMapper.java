package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "actorId", source = "viewerId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.POST_VIEW)")
    @Mapping(target = "receivedAt", source = "time")
    AnalyticsEvent toAnalyticsEvent(PostViewEvent postViewEvent);
}
