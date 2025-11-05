package faang.school.analytics.model;

import java.time.LocalDateTime;

public enum Interval {
    LAST_HOUR,
    LAST_DAY,
    LAST_WEEK,
    LAST_MONTH;

    public LocalDateTime getStart() {
        return switch (this) {
            case LAST_HOUR -> LocalDateTime.now().minusHours(1);
            case LAST_DAY -> LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
            case LAST_WEEK -> LocalDateTime.now().minusWeeks(1).toLocalDate().atStartOfDay();
            case LAST_MONTH -> LocalDateTime.now().minusMonths(1).toLocalDate().atStartOfDay();
        };
    }

    public LocalDateTime getEnd() {
        return LocalDateTime.now();
    }
}