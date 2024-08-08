package faang.school.analytics.validator;

import faang.school.analytics.exception.DataValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AnalyticsEventValidator {
    public void validateDates(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            String errorMessage = "Date FROM = " + from + " is greater than date TO = " + to;
            log.error(errorMessage);
            throw new DataValidationException(errorMessage);
        }
    }
}
