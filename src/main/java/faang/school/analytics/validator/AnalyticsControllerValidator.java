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
        if (eventTypeString.isBlank() && eventTypeInteger == null) {
            throw new DataValidationException("there is no eventType parameter in the request");
        }

        if (intervalString.isBlank() && intervalInteger == null
                && startDate.getDateTime() == null && endDate.getDateTime() == null) {
            throw new DataValidationException("there is no interval parameter in the request");
        }

        validateEventTypeFormatAndInteger(eventTypeString, eventTypeInteger);
        validateIntervalFormatAndInteger(eventTypeString, eventTypeInteger);
    }

    public void validateEventTypeFormatAndInteger(String eventTypeString, int eventTypeInteger) {
        try {
            EventType.valueOf(eventTypeString.toUpperCase(Locale.ROOT));
            EventType.of(eventTypeInteger);
        } catch (IllegalArgumentException|NullPointerException e) {
            throw new DataValidationException("there is no event with this type: " + eventTypeString + " " + eventTypeInteger);
        }
    }

    public void validateIntervalFormatAndInteger(String intervalString, Integer intervalInteger) {
        try {
            Interval.valueOf(intervalString);
            Interval.of(intervalInteger);
        } catch (IllegalArgumentException|NullPointerException e) {
            throw new DataValidationException("there is no such interval " + intervalString + " " + intervalInteger);
        }
    }
}
