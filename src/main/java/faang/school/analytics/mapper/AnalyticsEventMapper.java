package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.publishable.MentorshipRequestedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);
    @Mapping(source = "requesterId", target = "actorId")
    @Mapping(source = "timestamp", target = "receivedAt")
    @Mapping(target = "eventType", constant = "MENTORSHIP_REQUEST")
    AnalyticsEvent toEntity(MentorshipRequestedEvent dto);
}
