package faang.school.analytics.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnalyticsEventValidator {

    public void validateTimeBoundsPresence(String intervalStr, String fromStr, String toStr) {
        if (intervalStr == null && (fromStr == null || toStr == null)) {
            String errMessage = "There should be filtering Start and End or filtering Interval";
            log.error(errMessage);
            throw new IllegalArgumentException(errMessage);
        }
    }
}
