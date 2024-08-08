package faang.school.analytics.validator;

import faang.school.analytics.model.Interval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AnalyticsEventValidator {

    public void validateTimeBoundsPresence(Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            String errMessage = "There should be filtering Start and End or filtering Interval";
            log.error(errMessage);
            throw new IllegalArgumentException(errMessage);
        }
    }
}
