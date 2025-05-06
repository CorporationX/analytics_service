package faang.school.analytics.model;

import faang.school.analytics.exceptions.InvalidIntervalException;

import java.time.Clock;
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

    public LocalDateTime getStartDate(Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);
        return switch (this) {
            case LAST_HOUR -> now.minusHours(1);
            case TODAY -> now.toLocalDate().atStartOfDay();
            case YESTERDAY -> now.toLocalDate().minusDays(1).atStartOfDay();
            case LAST_WEEK -> now.toLocalDate().minusWeeks(1).atStartOfDay();
            case LAST_MONTH -> now.toLocalDate().minusMonths(1).atStartOfDay();
        };
    }

    public LocalDateTime getEndDate(Clock clock) {
        return LocalDateTime.now(clock);
    }
}
