package faang.school.analytics.model;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public enum Interval {
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public static Interval getInterval(String interval) {
        if (isOnlyDigits(interval)) {
            return byIndex(Integer.parseInt(interval));
        } else return byString(interval);
    }

    public static LocalDateTime getStartDateTimeByInterval(Interval interval) {
        return switch (interval) {
            case HOUR -> LocalDateTime.now().minusHours(1);
            case DAY -> LocalDateTime.now().minusDays(1);
            case WEEK -> LocalDateTime.now().minusWeeks(1);
            case MONTH -> LocalDateTime.now().minusMonths(1);
            case YEAR -> LocalDateTime.now().minusYears(1);
        };
    }

    private static Interval byIndex(int intervalIndex) {
        for (Interval i : Interval.values()) {
            if (i.ordinal() == intervalIndex) {
                return i;
            }
        }
        log.info("No such interval index : {}", intervalIndex);
        throw new IllegalArgumentException("No such interval index: " + intervalIndex);
    }

    private static Interval byString(String interval) {
        if (interval != null) {
            for (Interval i : Interval.values()) {
                if (interval.equalsIgnoreCase(i.toString())) {
                    return i;
                }
            }
        }
        log.info("Unknown interval value: {}", interval);
        throw new IllegalArgumentException("Unknown interval value: " + interval);
    }

    private static boolean isOnlyDigits(String interval) {
        boolean isDigit = true;
        for (int i = 0; i < interval.length() && isDigit; i++) {
            if (!Character.isDigit(interval.charAt(i))) {
                isDigit = false;
            }
        }
        return isDigit;
    }
}
