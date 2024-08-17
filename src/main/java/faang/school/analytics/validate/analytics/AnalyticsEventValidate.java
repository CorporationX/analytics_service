package faang.school.analytics.validate.analytics;

import faang.school.analytics.exception.ExceptionMessages;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@NoArgsConstructor
public class AnalyticsEventValidate {
    public void checkOfInputData(String intervalString, String fromDateString, String toDateString) {
        if (intervalString == null && fromDateString == null && toDateString == null) {
            log.error(ExceptionMessages.ARGUMENT_NOT_FOUND);
            throw new IllegalArgumentException(ExceptionMessages.ARGUMENT_NOT_FOUND);
        }
    }
}