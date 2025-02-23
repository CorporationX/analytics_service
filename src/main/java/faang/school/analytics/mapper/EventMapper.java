package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.event.AnalyticsRedisEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receivedAt", ignore = true)
    @Mapping(target = "receiverId", ignore = true)
    @Mapping(target = "actorId", ignore = true)
    @Mapping(target = "eventType", source = "type")
    @Mapping(target = "eventData", source = "data")
    AnalyticsEvent toAnalyticsEvent(AnalyticsRedisEvent event);
}
