package faang.school.analytics.dto.event;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public enum Interval {
    LAST_HOUR(LocalDateTime.now().minusHours(1)),
    TODAY(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)),
    YESTERDAY(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN),
            LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX));

    private final LocalDateTime start;
    private final LocalDateTime end;

    Interval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    Interval(LocalDateTime timePoint) {
        this(timePoint, LocalDateTime.now());
    }
}
