package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.event.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "viewerId")
    @Mapping(target = "receivedAt", source = "viewedAt")
    AnalyticsEvent toEntity(ProfileViewEvent event);

    AnalyticsEventDto toDto(AnalyticsEvent entity);
}
