package faang.school.analytics.model.enums;

import java.time.LocalDateTime;

public enum Interval {
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public static LocalDateTime getFromDate(Interval interval) {
        return switch (interval) {
            case DAY -> LocalDateTime.now().minusDays(1);
            case WEEK -> LocalDateTime.now().minusWeeks(1);
            case MONTH -> LocalDateTime.now().minusMonths(1);
            case YEAR -> LocalDateTime.now().minusYears(1);
        };
    }
}