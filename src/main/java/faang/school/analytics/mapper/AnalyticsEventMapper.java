package faang.school.analytics.mapper;

import faang.school.analytics.dto.recommendationevent.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventMapper {
    public AnalyticsEvent mapRecommendationToAnalyticsEvent(RecommendationEvent event) {
        return AnalyticsEvent.builder()
                .receiverId(event.getReceiverId())
                .actorId(event.getAuthorId())
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(event.getCreatedAt())
                .build();
    }
}
