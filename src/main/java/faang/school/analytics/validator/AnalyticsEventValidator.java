package faang.school.analytics.validator;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.interval.Interval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventValidator {
    public void validateAnalyticsEventParams(Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            throw new DataValidationException("необходимо заполнить интервал или период from-to");
        }
    }
}