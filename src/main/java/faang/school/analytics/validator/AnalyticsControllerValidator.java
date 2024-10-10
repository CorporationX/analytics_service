package faang.school.analytics.validator;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AnalyticsControllerValidator {
    public void validateRequestParams(String eventTypeString, Integer eventTypeInteger,
                                      String intervalString, Integer intervalInteger,
                                      LocalDateTimeInput startDate, LocalDateTimeInput endDate) {
        if (eventTypeString == null && eventTypeInteger == null) {
            throw new DataValidationException("there is no eventType parameter in the request");
        }

        if (intervalString == null && intervalInteger == null
                && (startDate == null || endDate == null)) {
            throw new DataValidationException("there is no interval parameter in the request");
        }

        if ((startDate != null && endDate != null) &&
                (startDate.getDateTime() == null && endDate.getDateTime() == null)) {
            throw new DataValidationException("zero time passed");
        }

        validateEventTypeFormatAndInteger(eventTypeString, eventTypeInteger);
        validateIntervalFormatAndInteger(intervalString, intervalInteger);
    }

    private void validateEventTypeFormatAndInteger(String eventTypeString, Integer eventTypeInteger) {
        try {
            if (eventTypeString != null) {
                EventType.valueOf(eventTypeString.toUpperCase(Locale.ROOT));
            } else {
                EventType.of(eventTypeInteger);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DataValidationException("there is no event with this type: " + eventTypeString + " " + eventTypeInteger);
        }
    }

    private void validateIntervalFormatAndInteger(String intervalString, Integer intervalInteger) {
        try {
            if (intervalString != null) {
                Interval.valueOf(intervalString);
            } else {
                Interval.of(intervalInteger);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DataValidationException("there is no such interval " + intervalString + " " + intervalInteger);
        }
    }
}
