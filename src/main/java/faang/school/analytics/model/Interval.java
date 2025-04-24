package faang.school.analytics.model;

import faang.school.analytics.exceptions.InvalidIntervalException;

import java.time.LocalDateTime;

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

    public LocalDateTime getStartDate() {
        return switch (this) {
            case LAST_HOUR -> LocalDateTime.now().minusHours(1);
            case TODAY -> LocalDateTime.now().toLocalDate().atStartOfDay();
            case YESTERDAY -> LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
            case LAST_WEEK -> LocalDateTime.now().minusWeeks(1).toLocalDate().atStartOfDay();
            case LAST_MONTH -> LocalDateTime.now().minusMonths(1).toLocalDate().atStartOfDay();
        };
    }

    public LocalDateTime getEndDate() {
        return LocalDateTime.now();
    }
}
