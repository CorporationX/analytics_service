package faang.school.analytics.service;

import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

public enum Interval {
    LAST_DAY,
    LAST_WEEK,
    LAST_MONTH,
    LAST_YEAR;

//    Pair<from, to>
    public Pair<LocalDateTime, LocalDateTime> getInterval() {
        return switch (this) {
            case LAST_DAY -> Pair.of(LocalDateTime.now().minusDays(1), LocalDateTime.now());
            case LAST_WEEK -> Pair.of(LocalDateTime.now().minusWeeks(1), LocalDateTime.now());
            case LAST_MONTH -> Pair.of(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
            case LAST_YEAR -> Pair.of(LocalDateTime.now().minusYears(1), LocalDateTime.now());
            default -> null;
        };

    }
}
