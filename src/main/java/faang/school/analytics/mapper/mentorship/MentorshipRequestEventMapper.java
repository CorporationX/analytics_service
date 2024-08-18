package faang.school.analytics.mapper.mentorship;

import faang.school.analytics.dto.event.mentorship.MentorshipRequestEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MentorshipRequestEventMapper {
    @Mapping(source = "requesterId", target = "actorId")
    @Mapping(source = "receiverId", target = "receiverId")
    @Mapping(source = "timestamp", target = "receivedAt")
    @Mapping(source = "eventId", target = "eventId")
    AnalyticsEvent toAnalyticsEntity(MentorshipRequestEvent mentorshipRequestEvent);
}
