package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.dto.event.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followingDate", target = "receivedAt")
    AnalyticsEventDto fromFollowerEventToDto(FollowerEvent event);
}