package faang.school.analytics.mapper;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventResponseDto;
import faang.school.analytics.event.SubscriptionEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventResponseDto entityToResponseDto(AnalyticsEvent event);

    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "subscribedAt", target = "receivedAt")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.FOLLOWER)")
    AnalyticsEvent toEntity(SubscriptionEvent event);

    AnalyticsEvent toEntity(AnalyticsEventResponseDto dto);
}
