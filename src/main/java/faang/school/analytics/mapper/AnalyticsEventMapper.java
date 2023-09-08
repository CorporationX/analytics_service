package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.dto.event.LikeEvent;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

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

    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
}