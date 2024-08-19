package faang.school.analytics.validator.analyticsEvent;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventValidator {

    public void allDateIntervalsAreEmpty(Interval interval, LocalDateTime from, LocalDateTime to) {
        if ((interval == null) && (from == null && to == null)) {
            throw new DataValidationException("All date intervals are empty");
        }
    }

    public void customIntervalIsValid(LocalDateTime from, LocalDateTime to) {
        if (!(from != null && to != null)) {
            throw new DataValidationException("Custom time range can't be empty");
        }

        if (from.isAfter(to)) {
            throw new DataValidationException("Incorrect custom date time range");
        }
    }
}
