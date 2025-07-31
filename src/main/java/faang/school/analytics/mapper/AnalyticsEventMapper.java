package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toEntity(AnalyticsEventDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventType", ignore = true)
    @Mapping(target = "receiverId", source = "publisherId")
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receivedAt", source = "followedAt")
    AnalyticsEvent toEntity(FollowerEvent event);

    AnalyticsEventDto toDto(AnalyticsEvent event);
}
