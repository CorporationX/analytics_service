package faang.school.analytics.mapper;

import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.stereotype.Component;

@Component
public class LikeEventMapper {

    public AnalyticsEvent toAnalyticsEvent(LikeEvent likeEvent) {
        return AnalyticsEvent.builder()
                .eventType(EventType.POST_LIKE)
                .actorId(likeEvent.getUserId())
                .receiverId(likeEvent.getPostId())
                .receivedAt(likeEvent.getLikeTime())
                .build();
    }
}
