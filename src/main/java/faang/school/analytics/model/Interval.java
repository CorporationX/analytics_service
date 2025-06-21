package faang.school.analytics.model;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Supplier;

public enum Interval {
    ONE_MONTH_AGO(() -> LocalDate.now().minusMonths(1)),
    ONE_WEEK_AGO(() -> LocalDate.now().minusWeeks(1)),
    THIS_MONTH(() -> LocalDate.now().withDayOfMonth(1)),
    THIS_WEEK(() -> LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))),
    TWO_MONTHS_AGO(() -> LocalDate.now().minusMonths(2)),
    TWO_WEEKS_AGO(() -> LocalDate.now().minusWeeks(2));

    private final Supplier<LocalDate> date;

    Interval(Supplier<LocalDate> date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date.get();
    }
}
