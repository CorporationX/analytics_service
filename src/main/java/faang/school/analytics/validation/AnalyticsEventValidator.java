package faang.school.analytics.validation;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventValidator {

    public void validateEventForSave(AnalyticsEvent analyticsEvent) {
        if (analyticsEvent == null) {
            throw new IllegalArgumentException("Analytics event can't be null");
        }
        if (analyticsEvent.getReceiverId() <= 0) {
            throw new IllegalArgumentException("ReceiverId must be positive");
        }
        if (analyticsEvent.getActorId() <= 0) {
            throw new IllegalArgumentException("ActorId must be positive");
        }
        if (analyticsEvent.getEventType() == null) {
            throw new IllegalArgumentException("EventType can't be null");
        }
        if (analyticsEvent.getReceivedAt() == null) {
            throw new IllegalArgumentException("ReceivedAt can't be null");
        }
        if (analyticsEvent.getReceivedAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("ReceivedAt can't be in the future");
        }
    }

    public void validateGetAnalyticsEventParams(long receiverId,
                                                EventType eventType,
                                                LocalDateTime from,
                                                LocalDateTime to,
                                                boolean intervalUsed) {
        if (receiverId <= 0) {
            throw new IllegalArgumentException("ReceiverId must be positive");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("EventType can't be null");
        }
        if (!intervalUsed) {
            if (from == null || to == null) {
                throw new IllegalArgumentException("From and To must be provided when interval is null");
            }
            if (from.isAfter(to)) {
                throw new IllegalArgumentException("To can't be earlier than From");
            }
            if (to.isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("To can't be in the future");
            }
        }
    }
}
