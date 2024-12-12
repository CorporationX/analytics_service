package faang.school.analytics.mapper.fund_raised;

import faang.school.analytics.dto.FundRaisedEvent;
import faang.school.analytics.dto.event.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FundRaisedEventMapper {

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "projectId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.DONATION)")
    @Mapping(source = "localDateTime", target = "receivedAt")
    EventDto toEventDto(FundRaisedEvent event);
}
