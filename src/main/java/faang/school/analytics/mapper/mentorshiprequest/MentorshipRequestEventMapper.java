package faang.school.analytics.mapper.mentorshiprequest;

import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.dto.analytic.mentorshiprequest.MentorshipRequestedEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MentorshipRequestEventMapper {
    EventType mentorship_requested = EventType.MENTORSHIP_REQUESTED;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "requesterId", target = "actorId")
    @Mapping(source = "receiverId", target = "receiverId")
    @Mapping(target = "eventTypeNumber", expression = "java(map(mentorship_requested))")
    AnalyticsEventDto mentorshipRequestedToAnalyticsDto(MentorshipRequestedEvent mentorshipRequestedEvent);

    @Named("mapToEventTypeNumber")
    default int map(EventType eventType) {
        return eventType.ordinal();
    }
}
