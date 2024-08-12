package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventValidator {
    public void validateAnalyticsEvent(AnalyticsEvent analyticsEvent) {
        if (analyticsEvent.getEventType() == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }
        if (analyticsEvent.getReceivedAt() == null) {
            throw new IllegalArgumentException("Datetime cannot be null");
        }
        if (analyticsEvent.getReceiverId() == 0L) {
            throw new IllegalArgumentException("ReceiverId cannot be 0");
        }
        if (analyticsEvent.getActorId() == 0L) {
            throw new IllegalArgumentException("ActorId cannot be 0");
        }
    }

    public void validateParams(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        if (receiverId == 0) {
            throw new IllegalArgumentException("ReceiverId cannot be 0");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }
        if (interval == null && (from == null || to == null)) {
            throw new IllegalArgumentException("Interval and time frames can't be null at same time");
        }
    }
}
