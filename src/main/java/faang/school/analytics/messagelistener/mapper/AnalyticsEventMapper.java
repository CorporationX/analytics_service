package faang.school.analytics.messagelistener.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.mentorshipevent.MentorshipRequestedEvent;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {


    AnalyticsEvent toAnalyticsEvent(MentorshipRequestedEvent mentorshipRequestedEvent);
}
