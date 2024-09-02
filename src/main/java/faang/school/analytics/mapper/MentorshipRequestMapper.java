package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.MentorshipRequestEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MentorshipRequestMapper extends GenericEventMapper<MentorshipRequestEvent, AnalyticsEventDto> {
    @Override
    @Mapping(source = "mentorId", target = "receiverId")
    @Mapping(source = "menteeId", target = "actorId")
    @Mapping(target = "eventType", constant = "MENTORSHIP_REQUEST")
    AnalyticsEventDto toEntity(MentorshipRequestEvent mentorshipRequestEvent);
}
