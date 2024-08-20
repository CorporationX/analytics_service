package faang.school.analytics.validator;

import faang.school.analytics.service.Interval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventControllerValidator {

    public boolean validateIntervalNotNull(Interval interval) {
        return interval != null;
    }

    public boolean validateFromToNotNull(LocalDateTime from, LocalDateTime to) {
        return from != null && to != null;
    }
}
