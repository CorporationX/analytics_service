package faang.school.analytics.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Supplier;

public enum Interval {
    ONE_MONTH_AGO(() -> LocalDate.now().minusMonths(1).atStartOfDay()),
    ONE_WEEK_AGO(() -> LocalDate.now().minusWeeks(1).atStartOfDay()),
    THIS_MONTH(() -> LocalDate.now().withDayOfMonth(1).atStartOfDay()),
    THIS_WEEK(() -> LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).atStartOfDay()),
    TWO_MONTHS_AGO(() -> LocalDate.now().minusMonths(2).atStartOfDay()),
    TWO_WEEKS_AGO(() -> LocalDate.now().minusWeeks(2).atStartOfDay());

    private final Supplier<LocalDateTime> date;

    Interval(Supplier<LocalDateTime> date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date.get();
    }
}
