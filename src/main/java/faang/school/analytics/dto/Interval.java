package faang.school.analytics.dto;

import java.time.LocalDateTime;

public enum Interval {
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public LocalDateTime getStartTime(final LocalDateTime endTime) {
        return switch (this) {
            case HOUR -> endTime.minusHours(1);
            case DAY -> endTime.minusDays(1);
            case WEEK -> endTime.minusWeeks(1);
            case MONTH -> endTime.minusMonths(1);
            case YEAR -> endTime.minusYears(1);
        };
    }
}