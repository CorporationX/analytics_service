package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MentorshipRequestedMapper {
    @Mappings({
            @Mapping(target = "receiverId", source = "receiverId"),
            @Mapping(target = "actorId", expression = "requesterId"),
            @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.MENTORSHIP_REQUESTED)"),
            @Mapping(target = "receivedAt", source = "timestamp")
    })
    AnalyticsEvent toAnalyticsEvent(MentorshipRequestedEvent mentorshipRequestedEvent);
}
