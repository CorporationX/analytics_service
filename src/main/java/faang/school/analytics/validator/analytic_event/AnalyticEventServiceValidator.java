package faang.school.analytics.validator.analytic_event;

import faang.school.analytics.dto.event.Interval;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticEventServiceValidator {
    public void checkEntity(AnalyticsEvent analyticsEvent) {
        if (analyticsEvent.getEventType() == null || analyticsEvent.getActorId() < 0 || analyticsEvent.getReceiverId() < 0) {
            throw new IllegalArgumentException("Не валидный eventType или actorId или ReceiverId ");
        }
    }

    public void validateInterval(Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            throw new IllegalArgumentException("Интервал и временной промежуток не может быть равен нулю");
        }
    }

    public void checkIdAndEvent(long id, EventType event) {
        if (id < 0 || event == null) {
            throw new IllegalArgumentException("не валидный id или эвент");
        }
    }
}
