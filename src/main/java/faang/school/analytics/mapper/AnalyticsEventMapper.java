package faang.school.analytics.mapper;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.model.FundRaisedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface AnalyticsEventMapper {

    AnalyticsEventDto toAnalyticsEventDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "receiverId", source = "projectId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "eventType", constant = "FUND_RAISED")
    @Mapping(target = "receivedAt", source = "donationTime")
    AnalyticsEvent toAnalyticsEvent(FundRaisedEvent event);
}
