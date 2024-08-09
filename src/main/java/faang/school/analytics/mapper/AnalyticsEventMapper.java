package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.CommentEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEvent commentEventToAnalyticsEvent(CommentEvent commentEvent);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> analyticsEvents);

//    long id;
//    long receiverId;
//    long actorId;
//    EventType eventType;
//    LocalDateTime receivedAt;

//    Long postId;
//    Long authorId;
//    Long commentId;
//    LocalDateTime sendAt;
}
