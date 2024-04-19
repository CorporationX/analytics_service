package faang.school.analytics.validation;

import faang.school.analytics.exception.DataValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsValidator {

    public void validateEventHavePeriod(String interval, LocalDateTime from, LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            throw new DataValidationException("Request must have period to analytics get");
        }
    }
}
