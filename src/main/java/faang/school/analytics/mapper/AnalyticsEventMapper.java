package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.LikeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "id", source = "postId")
    @Mapping(target = "receivedAt", source = "dateLike")
    AnalyticsEvent toEntity(LikeEvent likeEvent);
}


