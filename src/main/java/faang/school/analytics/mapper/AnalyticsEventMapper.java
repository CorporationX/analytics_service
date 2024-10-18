package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> events);

    AnalyticsEvent toAnalyticsEvent(ProfileViewEventDto analyticsEventService);

    List<AnalyticsEvent> toAnalyticsEvents(List<ProfileViewEventDto> analyticsEventService);

    @Mapping(target = "id", source = "postId", ignore = true)
    AnalyticsEvent postViewEventDtoToAnalyticsEvent(PostViewEventDto dto);
}