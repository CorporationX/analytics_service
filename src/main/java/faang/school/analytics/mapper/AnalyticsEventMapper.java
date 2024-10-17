package faang.school.analytics.mapper;

import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = EventType.class)
public interface AnalyticsEventMapper {
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "commentId", target = "receiverId")
    @Mapping(target = "eventType", expression = "java(EventType.POST_COMMENT)")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(CommentEvent commentEvent);
    
    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> events);

    AnalyticsEvent toAnalyticsEvent(ProfileViewEventDto analyticsEventService);

    List<AnalyticsEvent> toAnalyticsEvents(List<ProfileViewEventDto> analyticsEventService);
}
