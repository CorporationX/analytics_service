package faang.school.analytics.mapper;

import faang.school.analytics.kafka.events.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileViewEventMapper {

    @Mapping(source = "occurredAt", target = "receivedAt")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "eventTypeEnum", target = "eventType")
    @Mapping(target = "id", ignore = true)
    AnalyticsEvent toAnalyticsEvent(ProfileViewEvent profileViewEvent);
}
