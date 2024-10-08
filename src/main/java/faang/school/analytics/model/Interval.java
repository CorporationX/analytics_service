package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
public enum Interval {
    LAST_DAY(1, ChronoUnit.DAYS),
    LAST_WEEK(1, ChronoUnit.WEEKS),
    LAST_MONTH(1, ChronoUnit.MONTHS),
    LAST_THREE_MONTHS(3, ChronoUnit.MONTHS),
    LAST_SIX_MONTHS(6, ChronoUnit.MONTHS),
    YEAR_TO_DATE {
        @Override
        public LocalDateTime getFrom() {
            return LocalDateTime.of(LocalDate.now().getYear(), 1, 1, 0, 0);
        }

        @Override
        public LocalDateTime getTo() {
            return LocalDateTime.now();
        }
    };

    private final long amount;
    private final ChronoUnit unit;
    private final LocalDateTime from;
    private final LocalDateTime to;

    Interval() {
        this.amount = 0;
        this.unit = null;
        this.from = LocalDateTime.of(LocalDate.now().getYear(), 1, 1, 0, 0);
        this.to = LocalDateTime.now();
    }

    Interval(long amount, ChronoUnit unit) {
        this.amount = amount;
        this.unit = unit;
        this.from = calculateFrom();
        this.to = LocalDateTime.now();
    }

    private LocalDateTime calculateFrom() {
        if (unit != null) {
            return LocalDateTime.of(LocalDate.now().minus(amount, unit), LocalTime.MIN);
        }
        return LocalDateTime.now();
    }
}