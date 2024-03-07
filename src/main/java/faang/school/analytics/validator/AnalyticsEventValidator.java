package faang.school.analytics.validator;

import faang.school.analytics.exception.DataValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AnalyticsEventValidator {

    public void validateRequest(String interval, LocalDate fromDate, LocalDate toDate) {
        if (interval == null && ((fromDate == null) || (toDate == null))) {
            throw new DataValidationException("Заполните 1 из параметров : интервал или период");
        }
    }
}
