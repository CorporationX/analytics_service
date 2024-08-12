package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.events.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "viewedAt", target = "receivedAt")
    AnalyticsEventDto toDto(PostViewEvent postViewEvent);
}
