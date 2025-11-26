package faang.school.analytics.mapper;

import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventMapper {
    public AnalyticsEvent toEntity(LikeEvent event) {
        return AnalyticsEvent.builder()
                .receiverId(event.getAuthorId())
                .authorId(event.getLikedByUserId())
                .eventType(EventType.POST_LIKE)
                .receivedAt(event.getTimestamp())
                .build();
    }
}
