package faang.school.analytics.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
    DAY(TimeIntervalType::startOfDay, TimeIntervalType::endOfDay),
    WEEK(TimeIntervalType::startOfWeek, TimeIntervalType::endOfWeek),
    MONTH(TimeIntervalType::startOfMonth, TimeIntervalType::endOfMonth),
    YEAR(TimeIntervalType::startOfYear, TimeIntervalType::endOfYear);

    private final Function<LocalDateTime, LocalDateTime> start;
    private final Function<LocalDateTime, LocalDateTime> end;

    TimeIntervalType(Function<LocalDateTime, LocalDateTime> start,
                     Function<LocalDateTime, LocalDateTime> end) {
        this.start = start;
        this.end = end;
    }

    private static LocalDateTime startOfDay(LocalDateTime date) {
        return date.toLocalDate().atStartOfDay();
    }

    private static LocalDateTime endOfDay(LocalDateTime date) {
        return date.toLocalDate().atTime(LocalTime.MAX);
    }

    private static LocalDateTime startOfWeek(LocalDateTime date) {
        return date.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
    }

    private static LocalDateTime endOfWeek(LocalDateTime date) {
        return date.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
    }

    private static LocalDateTime startOfMonth(LocalDateTime date) {
        return date.toLocalDate().withDayOfMonth(1).atStartOfDay();
    }

    private static LocalDateTime endOfMonth(LocalDateTime date) {
        return date.toLocalDate().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
    }

    private static LocalDateTime startOfYear(LocalDateTime date) {
        return date.toLocalDate().withDayOfYear(1).atStartOfDay();
    }

    private static LocalDateTime endOfYear(LocalDateTime date) {
        return date.toLocalDate().with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX);
    }

    public LocalDateTime getStart(LocalDateTime date) { return start.apply(date); }
    public LocalDateTime getEnd(LocalDateTime date) { return end.apply(date); }

    public LocalDateTime[] getRange(LocalDateTime date) {
        return new LocalDateTime[]{getStart(date), getEnd(date)};
    }

    public LocalDateTime[] getRange() {
        return getRange(LocalDateTime.now());
    }
}
