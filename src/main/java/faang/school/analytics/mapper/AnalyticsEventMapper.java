package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "viewerId", target = "actorId")
    @Mapping(target = "eventType", ignore = true)
    @Mapping(source = "viewTime", target = "receivedAt")
    AnalyticsEvent toEntity(PostViewEvent postViewEvent);
}
