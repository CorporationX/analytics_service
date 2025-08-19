package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "eventType", constant = "MENTORSHIP_REQUESTED")
    AnalyticsEvent toAnalyticsEvent(MentorshipRequestedEvent mentorshipRequestedEvent);
}
