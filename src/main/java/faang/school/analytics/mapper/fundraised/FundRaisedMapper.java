package faang.school.analytics.mapper.fundraised;

import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.fundraised.FundRaisedEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FundRaisedMapper {
    EventType PROJECT_DONATION = EventType.PROJECT_DONATION;

    @Mapping(target = "receiverId", source = "projectId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "eventTypeNumber", expression = "java(map(PROJECT_DONATION))")
    @Mapping(target = "receivedAt", source = "donationTime")
    AnalyticsEventDto fundRaisedtoAnalyticsEventDto(FundRaisedEvent fundRaisedEvent);

    default int map(EventType eventType) {
        return eventType.ordinal();
    }
}
