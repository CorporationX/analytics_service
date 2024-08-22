package faang.school.analytics.validator;

import faang.school.analytics.exception.IllegalModelException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventValidator {
    public void validatePresenceOfIntervalOrDateRange(String interval, LocalDateTime from, LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            throw new IllegalModelException("Either 'interval' must be provided, or both 'from' and 'to' must be specified.");
        }
    }
}
