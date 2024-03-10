package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MentorshipRequestedMapper extends AnalyticsEventMapper<MentorshipRequestedEventDto> {

    @Mapping(source = "requesterId", target = "actorId")
    AnalyticsEvent toAnalyticsEvent(MentorshipRequestedEventDto event);

    @Override
    default Class<MentorshipRequestedEventDto> getType() {
        return MentorshipRequestedEventDto.class;
    }
}
