package faang.school.analytics.mapper.analytics;

import faang.school.analytics.kafka.events.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventMapper {

    public AnalyticsEvent fromEvent(RecommendationEvent event) {
        AnalyticsEvent ae = new AnalyticsEvent();
        ae.setId(event.getId());
        ae.setActorId(event.getAuthorId());
        ae.setReceiverId(event.getRecipientId());
        ae.setReceivedAt(event.getTimestamp());
        return ae;
    }
}
