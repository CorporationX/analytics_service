package faang.school.analytics;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.redis.event.LikeEvent;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(source = "likeEvent.idAuthor", target = "receiverId")
    @Mapping(source = "likeEvent.idUser", target = "actorId")
    @Mapping(source = "likeEvent.dateTime", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(LikeEvent likeEvent);

    @AfterMapping
    default void setLikeType(@MappingTarget AnalyticsEvent analyticsEvent) {
        analyticsEvent.setEventType(EventType.POST_LIKE);
    }
}