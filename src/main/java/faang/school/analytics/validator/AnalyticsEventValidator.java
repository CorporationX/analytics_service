package faang.school.analytics.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnalyticsEventValidator {

    public void validateIntervalOrPeriod(String interval, String startTime, String endTime) {
        if ((interval == null || interval.isEmpty()) &&
                (startTime == null || startTime.isEmpty() && endTime == null || endTime.isEmpty())) {
            log.info("Interval or startTime and endTime is invalid");
            throw new IllegalArgumentException("Interval or startTime and endTime is invalid");
        }
    }
}
