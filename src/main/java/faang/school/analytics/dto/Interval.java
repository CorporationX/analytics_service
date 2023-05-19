package faang.school.analytics.dto;

import java.time.LocalDateTime;

public enum Interval {
    HOUR, DAY, WEEK, MONTH, YEAR;

    public static Interval fromString(String value) {
        return valueOf(value.toUpperCase());
    }

    public LocalDateTime getFrom() {
        return switch (this) {
            case HOUR -> LocalDateTime.now().minusHours(1);
            case DAY -> LocalDateTime.now().minusDays(1);
            case WEEK -> LocalDateTime.now().minusWeeks(1);
            case MONTH -> LocalDateTime.now().minusMonths(1);
            case YEAR -> LocalDateTime.now().minusYears(1);
        };
    }
}
