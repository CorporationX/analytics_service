package faang.school.analytics.mapper.like;

import faang.school.analytics.dto.event.like.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeEventMapper {

    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "actorId", source = "authorId")
    AnalyticsEvent toAnalyticsEventEntity(LikeEvent likeEvent);
}
