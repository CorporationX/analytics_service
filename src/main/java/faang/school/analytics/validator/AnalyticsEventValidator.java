package faang.school.analytics.validator;

import faang.school.analytics.exception.DataValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventValidator {

    public void validateDateOrder(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new DataValidationException(
                    String.format("'From' %s date must be earlier than 'To' %s date.", from, to));
        }
    }
}
