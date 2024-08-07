package faang.school.analytics.filters.timeManagment;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public enum Interval {
    MORNING(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusHours(6), LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusHours(12)),
    AFTERNOON(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusHours(12), LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusHours(18)),
    EVENING(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusHours(18), LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusHours(24)),
    FROM_YESTERDAY_THIS_TIME_TIL_NOW(LocalDateTime.now().minusDays(1), LocalDateTime.now()),
    ONE_WEEK_BEFORE_TIL_NOW(LocalDateTime.now().minusWeeks(1), LocalDateTime.now()),
    TWO_WEEKS_BEFORE_TIL_NOW(LocalDateTime.now().minusWeeks(2), LocalDateTime.now()),
    TREE_WEEKS_BEFORE_TIL_NOW(LocalDateTime.now().minusWeeks(3), LocalDateTime.now()),
    ONE_MONTH_BEFORE_TIL_NOW(LocalDateTime.now().minusMonths(1), LocalDateTime.now()),
    SIX_MONTHS_BEFORE_TIL_NOW(LocalDateTime.now().minusMonths(6), LocalDateTime.now()),
    ONE_YEAR_BEFORE_TIL_NOW(LocalDateTime.now().minusYears(1), LocalDateTime.now());

    private final LocalDateTime start;
    private final LocalDateTime end;

    Interval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public static Interval of(String intervalStr) {
        for (Interval interval : Interval.values()) {
            if (intervalStr.equalsIgnoreCase(interval.name())) {
                return interval;
            }
        }
        return null;
    }

    public static Interval of(int intervalPos) {
        for (Interval interval : Interval.values()) {
            if (intervalPos == interval.ordinal()) {
                return interval;
            }
        }
        return null;
    }

}
