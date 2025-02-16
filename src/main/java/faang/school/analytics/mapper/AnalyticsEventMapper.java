package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.event.AnalyticsCommentEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(target = "eventType", constant = "POST_COMMENT")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(AnalyticsCommentEvent event);
}
