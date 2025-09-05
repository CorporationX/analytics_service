package faang.school.analytics.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Function;

/**
 * Класс для определения временных интервалов
 *
 * @author Linempy
 * @since 20.08.2025
 */
public enum TimeIntervalType {
    DAY(LocalDate::atStartOfDay,
            d -> d.atTime(LocalTime.MAX)),

    WEEK(d -> d.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay(),
            d -> d.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX)),

    MONTH(d -> d.withDayOfMonth(1).atStartOfDay(),
            d -> d.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX)),

    YEAR(d -> d.withDayOfYear(1).atStartOfDay(),
            d -> d.with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX));

    private final Function<LocalDate, LocalDateTime> startDateCalculator;
    private final Function<LocalDate, LocalDateTime> endDateCalculator;

    TimeIntervalType(Function<LocalDate, LocalDateTime> startDateCalculator,
                     Function<LocalDate, LocalDateTime> endDateCalculator) {
        this.startDateCalculator = startDateCalculator;
        this.endDateCalculator = endDateCalculator;
    }

    public LocalDateTime getStartDate(LocalDateTime referenceDate) {
        return startDateCalculator.apply(referenceDate.toLocalDate());
    }

    public LocalDateTime getEndDate(LocalDateTime referenceDate) {
        return endDateCalculator.apply(referenceDate.toLocalDate());
    }

    public LocalDateTime[] getDateRange(LocalDateTime referenceDate) {
        return new LocalDateTime[]{
                getStartDate(referenceDate),
                getEndDate(referenceDate)
        };
    }

    public LocalDateTime[] getDateRange() {
        return getDateRange(LocalDateTime.now());
    }
}
