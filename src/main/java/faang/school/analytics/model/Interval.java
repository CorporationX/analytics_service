package faang.school.analytics.model;

import faang.school.analytics.exceptions.InvalidIntervalException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public enum Interval {
    LAST_HOUR,
    TODAY,
    YESTERDAY,
    LAST_WEEK,
    LAST_MONTH;

    public static Interval from(Object rawValue) {
        if (rawValue instanceof String string) {
            return Interval.valueOf(string.toUpperCase());
        } else if (rawValue instanceof Integer num) {
            return Interval.values()[num];
        }
        throw new InvalidIntervalException("Invalid interval: " + rawValue);
    }

    public LocalDateTime getStartDate(ZoneId zoneId) {
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        return switch (this) {
            case LAST_HOUR -> now.minusHours(1).toLocalDateTime();
            case TODAY -> now.toLocalDate().atStartOfDay();
            case YESTERDAY -> now.minusDays(1).toLocalDate().atStartOfDay();
            case LAST_WEEK -> now.minusWeeks(1).toLocalDate().atStartOfDay();
            case LAST_MONTH -> now.minusMonths(1).toLocalDate().atStartOfDay();
        };
    }

    public LocalDateTime getEndDate(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId).toLocalDateTime();
    }
}
