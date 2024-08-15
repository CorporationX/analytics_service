package faang.school.analytics.mapper;

import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    default LikeEvent toLikeEvent(AnalyticsEvent analyticsEvent) {
        return LikeEvent.builder()
                .postId(analyticsEvent.getId())
                .userId(analyticsEvent.getReceiverId())
                .authorLikeId(analyticsEvent.getActorId())
                .localDateTime(analyticsEvent.getReceivedAt())
                .build();
    }

    default AnalyticsEvent toAnalyticsEventFromLikeEvent(LikeEvent likeEvent) {
        return  AnalyticsEvent.builder()
                .id(likeEvent.getPostId())
                .receiverId(likeEvent.getUserId())
                .actorId(likeEvent.getAuthorLikeId())
                .eventType(EventType.POST_LIKE)
                .receivedAt(likeEvent.getLocalDateTime())
                .build();
    }
}