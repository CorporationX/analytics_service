package faang.school.analytics.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public enum Interval {
    LAST_HOUR(LocalDateTime.now().minusHours(1), LocalDateTime.now()
    ),
    LAST_DAY(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN),
            LocalDateTime.now()
    ),
    LAST_WEEK(LocalDateTime.of(LocalDate.now().minusWeeks(1), LocalTime.MIN),
            LocalDateTime.now()
    ),
    LAST_MONTH(LocalDateTime.of(LocalDate.now().minusMonths(1), LocalTime.MIN),
            LocalDateTime.now()
    );

    private final LocalDateTime start;
    private final LocalDateTime end;

    Interval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
