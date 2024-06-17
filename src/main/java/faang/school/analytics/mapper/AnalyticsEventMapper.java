package faang.school.analytics.mapper;

import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "likeUserId", target = "actorId")
    AnalyticsEvent toEntity(LikeEvent event);
}
