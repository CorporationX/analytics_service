package faang.school.analytics.service;

import faang.school.analytics.dto.analyticsEvent.AnalyticsRequestParams;
import faang.school.analytics.exception.exceptions.InvalidDateException;
import faang.school.analytics.exception.exceptions.InvalidEventTypeException;
import faang.school.analytics.exception.exceptions.InvalidIntervalException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class AnalyticsRequestService {
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public EventType convertToEventType(String eventType) {
        try {
            int index = Integer.parseInt(eventType);
            return EventType.values()[index];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            try {
                return EventType.valueOf(eventType.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new InvalidEventTypeException("Invalid EventType: " + eventType);
            }
        }
    }

    public Interval convertToInterval(String interval) {
        try {
            int index = Integer.parseInt(interval);
            return Interval.values()[index];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            try {
                return Interval.valueOf(interval.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new InvalidIntervalException("Invalid Interval: " + interval);
            }
        }
    }

    public LocalDateTime convertToLocalDateTime(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("Invalid date format. Expected format: " + DATE_PATTERN);
        }
    }

    public AnalyticsRequestParams processRequestParams(String eventType, String interval, String from, String to) {
        EventType eventTypeEnum = convertToEventType(eventType);
        Interval intervalEnum = (interval != null) ? convertToInterval(interval) : null;
        LocalDateTime fromDateTime = (from != null) ? convertToLocalDateTime(from) : null;
        LocalDateTime toDateTime = (to != null) ? convertToLocalDateTime(to) : null;

        validateRequest(intervalEnum, fromDateTime, toDateTime);

        return new AnalyticsRequestParams(eventTypeEnum, intervalEnum, fromDateTime, toDateTime);
    }

    private void validateRequest(Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            throw new IllegalArgumentException("Either 'interval' must be specified or both 'from' and 'to' dates must be provided.");
        }
    }
}