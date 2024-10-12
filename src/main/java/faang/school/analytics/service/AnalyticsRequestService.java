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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class AnalyticsRequestService {
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public AnalyticsRequestParams processRequestParams(String eventType, String interval, String from, String to) {
        EventType eventTypeEnum = convertToEventType(eventType);

        Interval intervalEnum;
        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;

        if (interval != null && !interval.isEmpty()) {
            if (from != null || to != null) {
                throw new IllegalArgumentException("Нельзя указывать 'from' или 'to', когда задан 'interval'.");
            }
            intervalEnum = convertToInterval(interval);
        } else {
            intervalEnum = Interval.ALL_TIME;

            fromDateTime = (from != null && !from.isEmpty())
                    ? convertToLocalDateTime(from)
                    : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
            toDateTime = (to != null && !to.isEmpty())
                    ? convertToLocalDateTime(to)
                    : LocalDateTime.now();

            if (fromDateTime.isAfter(toDateTime)) {
                throw new IllegalArgumentException("'from' дата не может быть позже 'to' даты.");
            }
        }
        return new AnalyticsRequestParams(eventTypeEnum, intervalEnum, fromDateTime, toDateTime);
    }

    public EventType convertToEventType(String eventType) {
        try {
            return EventType.valueOf(eventType.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidEventTypeException("Invalid EventType: " + eventType);
        }
    }

    public Interval convertToInterval(String interval) {
        try {
            return Interval.valueOf(interval.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidIntervalException("Invalid Interval: " + interval);
        }
    }

    public LocalDateTime convertToLocalDateTime(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("Invalid date format. Expected format: " + DATE_PATTERN);
        }
    }
}