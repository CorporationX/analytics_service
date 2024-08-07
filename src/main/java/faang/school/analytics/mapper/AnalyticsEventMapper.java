package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.FollowerEvent;
import faang.school.analytics.model.ProfileViewEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEvent followerEventToAnalyticsEvent(FollowerEvent followerEvent);

    AnalyticsEvent profileViewEventToAnalyticsEvent(ProfileViewEvent profileViewEvent);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> analyticsEvents);
}
