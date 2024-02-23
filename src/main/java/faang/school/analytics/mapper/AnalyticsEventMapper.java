package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AnalyticsEventMapper {

    @Mapping(source = "requesterId", target = "actorId")
    AnalyticsEvent toEntity(MentorshipRequestedEventDto eventDto);

    MentorshipRequestedEventDto toDto(AnalyticsEvent analyticsEvent);
}
