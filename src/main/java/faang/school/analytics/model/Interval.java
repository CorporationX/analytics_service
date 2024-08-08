package faang.school.analytics.model;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

public enum Interval {
    SECOND,
    MINUTE,
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public TemporalAmount getTemporalAmount() {
        switch (this) {
            case SECOND -> {
                return Duration.ofSeconds(1);
            }
            case MINUTE -> {
                return Duration.ofMinutes(1);
            }
            case HOUR -> {
                return Duration.ofHours(1);
            }
            case DAY -> {
                return Period.ofDays(1);
            }
            case WEEK -> {
                return Period.ofWeeks(1);
            }
            case MONTH -> {
                return Period.ofMonths(1);
            }
            case YEAR -> {
                return Period.ofYears(1);
            }
            default -> {
                throw new IllegalArgumentException("Unknown interval " + this);
            }
        }
    }

    public static Interval fromString(String value) {
        for (Interval interval : Interval.values()) {
            if (interval.name().equalsIgnoreCase(value)) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown interval type: " + value);
    }

    public static Interval of(int type) {
        for (Interval interval : Interval.values()) {
            if (interval.ordinal() == type) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown interval type: " + type);
    }
}
