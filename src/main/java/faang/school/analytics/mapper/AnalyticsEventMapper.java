package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "receiverId", source = "commentEvent.postId")
    @Mapping(target = "actorId", source = "commentEvent.authorId")
    @Mapping(target = "eventType", constant = "COMMENT")
    @Mapping(target = "receivedAt", source = "commentEvent.date")
    AnalyticsEvent toAnalyticsEventFromCommentEvent(CommentEvent commentEvent);
}
