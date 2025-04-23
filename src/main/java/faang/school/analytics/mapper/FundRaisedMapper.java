package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FundRaisedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FundRaisedMapper {

    @Mapping(source = "projectId", target = "actorId")
    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "raiseDate", target = "receivedAt")
    @Mapping(target = "eventType", constant = "FUND_RAISED")
    AnalyticsEventDto fundRaisedToAnalyticEventDto(FundRaisedEvent fundRaisedEvent);
}
