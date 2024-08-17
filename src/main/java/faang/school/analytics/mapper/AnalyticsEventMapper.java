package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "actorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(PostLikeEvent postLikeEvent);

}
