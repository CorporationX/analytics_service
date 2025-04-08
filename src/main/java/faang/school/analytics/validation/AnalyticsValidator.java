package faang.school.analytics.validation;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.dto.AnalyticsRequest;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsValidator {

    public void validate(AnalyticsRequest request) {
        validateInterval(request.getInterval(), request.getFrom(), request.getTo());
        validateEventType(request.getType());
    }

    private void validateInterval(Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            throw new DataValidationException(
                    "The 'from' and 'to' parameters must not be 'null' unless an interval is specified");
        }

        if (from != null && to != null && from.isAfter(to)) {
            throw new DataValidationException(
                    "The 'from' date must be before or equal to the 'to' date");
        }

        if (interval != null && (from != null || to != null)) {
            throw new DataValidationException(
                    "Either specify an interval or from/to dates, but not both");
        }
    }

    private void validateEventType(EventType eventType) {
        if (eventType == null) {
            throw new DataValidationException("Event type cannot be 'null'");
        }
    }

}
