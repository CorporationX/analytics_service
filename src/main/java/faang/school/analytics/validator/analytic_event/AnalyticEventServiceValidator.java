package faang.school.analytics.validator.analytic_event;

import faang.school.analytics.dto.event.EventRequestDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

@Component
public class AnalyticEventServiceValidator {
    public void checkEntity(AnalyticsEvent analyticsEvent) {
        if (analyticsEvent.getEventType() == null || analyticsEvent.getActorId() < 0 || analyticsEvent.getReceiverId() < 0) {
            throw new IllegalArgumentException("Не валидный eventType или actorId или ReceiverId ");
        }
    }

    public void checkRequestDto(EventRequestDto eventRequestDto) {
        if (eventRequestDto.getInterval() == null && (eventRequestDto.getTo() == null || eventRequestDto.getFrom() == null)) {
            throw new IllegalArgumentException("Интервал и временной промежуток не может быть равен нулю");
        }
    }
}
