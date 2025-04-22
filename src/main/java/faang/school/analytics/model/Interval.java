package faang.school.analytics.model;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * Перечисление, представляющее временные интервалы для выборки данных.
 * <p>
 * Каждая константа содержит логику вычисления начальной даты интервала
 * относительно текущего момента времени.
 */
@Slf4j
public enum Interval {
    LAST_DAY(() -> LocalDateTime.now().minusDays(1)),
    LAST_WEEK(() -> LocalDateTime.now().minusWeeks(1)),
    LAST_MONTH(() -> LocalDateTime.now().minusMonths(1)),
    LAST_QUARTER(() -> LocalDateTime.now().minusMonths(3)),
    LAST_YEAR(() -> LocalDateTime.now().minusYears(1));

    private final Supplier<LocalDateTime> supplier;

    Interval(Supplier<LocalDateTime> supplier) {
        this.supplier = supplier;
    }

    /**
     * Возвращает начальную дату интервала.
     *
     * @return {@link LocalDateTime}, указывающую на начало интервала
     */
    public LocalDateTime getStart() {
        return supplier.get();
    }

    /**
     * Возвращает константу {@link Interval} по её порядковому номеру.
     *
     * @param type порядковый номер интервала (начиная с 0 для {@link #LAST_DAY})
     * @return соответствующая константа перечисления
     * @throws IllegalArgumentException если передан несуществующий номер
     */
    public static Interval of(int type) {
        for (Interval interval : values()) {
            if (interval.ordinal() == type) {
                return interval;
            }
        }
        log.error("Unknown interval type requested: {}", type);
        throw new IllegalArgumentException("Unknown event type: " + type);
    }
}
