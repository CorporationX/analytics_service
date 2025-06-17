package faang.school.analytics.mapper;

import faang.school.analytics.kafka.events.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileViewEventMapper {
    @Mapping(source = "localDateTime", target = "receivedAt")
    @Mapping(source = "viewedUserId", target = "receiverId")
    @Mapping(source = "viewerUserId", target = "actorId")
    @Mapping(source = "eventType", target = "eventType")
    AnalyticsEvent toAnalyticsEvent(ProfileViewEvent profileViewEvent);
}
