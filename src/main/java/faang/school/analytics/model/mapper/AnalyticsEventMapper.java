package faang.school.analytics.model.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.dto.PostViewEvent;
import faang.school.analytics.model.dto.ProfileViewEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(source = "eventType", target = "eventType", qualifiedByName = "eventTypeToString")
    @Mapping(source = "receivedAt", target = "receivedAt")
    AnalyticsEventDto toDto(AnalyticsEvent event);

    @Mapping(source = "eventType", target = "eventType", qualifiedByName = "stringToEventType")
    @Mapping(source = "receivedAt", target = "receivedAt")
    AnalyticsEvent toEntity(AnalyticsEventDto dto);

    @Mapping(target = "eventType", constant = "PROFILE_VIEW")
    @Mapping(source = "createdTime", target = "receivedAt")
    @Mapping(source = "idUser", target = "receiverId")
    @Mapping(source = "idRequester", target = "actorId")
    AnalyticsEvent toEntityFromProfileViewEvent(ProfileViewEvent profileViewEvent);

    @Mapping(target = "eventType", constant = "POST_VIEW")
    @Mapping(source = "timestamp", target = "receivedAt")
    @Mapping(source = "authorId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    AnalyticsEvent toEntityFromPostViewEvent(PostViewEvent postViewEvent);

    @Named("eventTypeToString")
    static String eventTypeToString(EventType eventType) {
        return eventType != null ? eventType.name() : null;
    }

    @Named("stringToEventType")
    static EventType stringToEventType(String eventType) {
        return eventType != null ? EventType.valueOf(eventType) : null;
    }
}