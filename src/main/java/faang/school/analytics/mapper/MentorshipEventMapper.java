package faang.school.analytics.mapper;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MentorshipEventMapper {

    @Mapping(target = "actorId", source = "requesterId")
    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "receivedAt", source = "createdAt")
    EventDto toDto(MentorshipEventDto mentorshipEventDto);

    @Mapping(target = "actorId", source = "requesterId")
    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "receivedAt", source = "createdAt")
    AnalyticsEvent toEntity(MentorshipEventDto mentorshipEventDto);
}
