package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "eventType", expression = "java(EventType.POST_COMMENT)")
    @Mapping(target = "receiverId", source = "authorId")
    @Mapping(target = "actorId", source = "commentId")
    @Mapping(target = "receivedAt", source = "date")
    AnalyticsEvent toAnalytics(CommentEvent commentEvent);
}
