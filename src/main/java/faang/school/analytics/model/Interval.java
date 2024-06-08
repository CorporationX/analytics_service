package faang.school.analytics.model;

import java.time.LocalDateTime;
import java.util.Arrays;

public enum Interval {

    YEAR,
    MONTH,
    WEEK,
    DAY;

    public static LocalDateTime getFromDate(Interval interval) {
        return switch (interval) {
            case YEAR -> LocalDateTime.now().minusYears(1);
            case MONTH -> LocalDateTime.now().minusMonths(1);
            case WEEK -> LocalDateTime.now().minusWeeks(1);
            case DAY -> LocalDateTime.now().minusDays(1);
        };
    }

    public static Interval of(int type) {
        for (Interval interval : Interval.values()) {
            if (interval.ordinal() == type) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown interval: " + type);
    }

    public static Interval getFromString(String interval) {

        if (interval == null) {
            return null;
        }

        boolean canParse = Arrays.stream(Interval.values()).anyMatch(s -> s.name().equalsIgnoreCase(interval));

        if (canParse) {
            return Interval.valueOf(interval);
        } else {
            try {
                return of(Integer.parseInt(interval));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unknown interval: " + interval);
            }
        }
    }
}
