package faang.school.analytics.dto;

import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(source = "authorCommentId", target = "actorId")
    @Mapping(source = "authorPostId", target = "receiverId")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(CommentEvent commentEvent);
}
