package faang.school.analytics.validator;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class AnalyticControllerValidator {
    public void validateIntervalAndDates(String interval, String start, String end) {
        if ((interval == null || interval.isBlank()) &&
                (start == null || start.isBlank() || end == null || end.isBlank())) {
            throw new ValidationException("Interval and dates cannot be null at the same time.");
        }
    }
}
