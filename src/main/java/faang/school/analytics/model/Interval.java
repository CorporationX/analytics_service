package faang.school.analytics.model;

import java.time.LocalDateTime;

public enum Interval {
    LAST_HOUR,
    LAST_DAY,
    LAST_WEEK,
    LAST_MONTH;

    public boolean isWithin(LocalDateTime eventDate) {
        LocalDateTime current = LocalDateTime.now();

        return switch(this) {
            case LAST_HOUR -> eventDate.isAfter(current.minusHours(1));
            case LAST_DAY -> eventDate.isAfter(current.minusDays(1));
            case LAST_WEEK -> eventDate.isAfter(current.minusWeeks(1));
            case LAST_MONTH -> eventDate.isAfter(current.minusMonths(1));
        };
    }
}
