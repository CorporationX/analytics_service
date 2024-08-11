package faang.school.analytics.util;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public enum Interval {
    LAST_DAY(now().minusDays(1), now()),
    LAST_WEEK(now().minusWeeks(1), now()),
    LAST_MONTH(now().minusMonths(1), now()),
    LAST_SIX_MONTHS(now().minusMonths(6), now()),
    LAST_YEAR(now().minusYears(1), now());

    LocalDateTime start;
    LocalDateTime end;

    Interval(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.start = start;
        this.end = end;
    }

    public boolean contains(LocalDateTime dateTime) {
        return (dateTime.isEqual(start) || dateTime.isAfter(start)) &&
                (dateTime.isEqual(end) || dateTime.isBefore(end));
    }
}
