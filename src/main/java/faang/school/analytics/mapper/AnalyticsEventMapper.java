package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.event.analyticsEvent.AnalyticsEventRequestDto;
import faang.school.analytics.dto.event.likeEvent.PostLikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEventRequestDto toRequestDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(target = "eventType", ignore = true)
    @Mapping(target = "receivedAt", ignore = true)
    AnalyticsEvent toEntity(AnalyticsEventRequestDto analyticsEventRequestDto);

    @Mapping(target = "receiverId", source = "authorId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "timestamp")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.POST_LIKE)")
    AnalyticsEvent toAnalyticsFromLike(PostLikeEvent postLikeEvent);

    List<AnalyticsEventDto> toDto(List<AnalyticsEvent> analyticsEvents);

    List<AnalyticsEvent> toEntity(List<AnalyticsEventDto> analyticsEventDtos);
}
