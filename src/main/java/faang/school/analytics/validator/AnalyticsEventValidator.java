package faang.school.analytics.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventValidator {


    public void validate(String interval, LocalDateTime from, LocalDateTime to) {

        if (interval == null) {
            Assert.isTrue(from != null && to != null, "interval and from/to cannot be null when interval is null");
        }
    }
}
