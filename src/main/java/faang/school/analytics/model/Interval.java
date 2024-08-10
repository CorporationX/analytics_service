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

    public TemporalAmount getTemporalAmount(Interval interval) {
        switch (interval) {
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
                throw new IllegalArgumentException("Unknown interval " + interval);
            }
        }
    }
}
