package faang.school.analytics.mapper;

import faang.school.analytics.event.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostViewEventMapper {

    @Mapping(source = "authorId", target = "receiverId")
    @Mapping(source = "viewerId", target = "actorId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.POST_VIEW)")
    AnalyticsEvent toAnalyticsEvent(PostViewEvent postViewEvent);
}

