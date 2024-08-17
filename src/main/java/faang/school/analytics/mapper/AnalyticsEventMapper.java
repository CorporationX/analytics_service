package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import faang.school.analytics.model.LikeEvent;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "authorId", target = "receiverId")
    @Mapping(source = "eventType", target = "eventType")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent likeEventToAnalyticsEvent(LikeEvent likeEvent);
}
