package faang.school.analytics.model;

import faang.school.analytics.exceptions.InvalidIntervalException;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

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

    public Instant getStartDate(Clock clock) {
        ZonedDateTime now = ZonedDateTime.now(clock);
        return switch (this) {
            case LAST_HOUR -> now.minusHours(1).toInstant();
            case TODAY -> now.truncatedTo(ChronoUnit.DAYS).toInstant();
            case YESTERDAY -> now.truncatedTo(ChronoUnit.DAYS).minusDays(1).toInstant();
            case LAST_WEEK -> now.truncatedTo(ChronoUnit.DAYS).minusWeeks(1).toInstant();
            case LAST_MONTH -> now.truncatedTo(ChronoUnit.DAYS).minusMonths(1).toInstant();
        };
    }

    public Instant getEndDate(Clock clock) {
        return Instant.now(clock);
    }
}
