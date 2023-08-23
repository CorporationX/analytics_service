package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.dto.MentorshipRequestedEventDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MentorshipRequestedEventMapper {

    @Mapping(target = "actorId", source = "requesterId")
    @Mapping(target ="receivedAt", source = "createdAt")
    AnalyticsEvent toEntity(MentorshipRequestedEventDto analyticsEventDto);

    @Mapping(target = "actorId", source = "requesterId")
    @Mapping(target ="createdAt", source = "createdAt" )
    AnalyticsEventDto toDto(MentorshipRequestedEventDto analyticsEvent);
}
