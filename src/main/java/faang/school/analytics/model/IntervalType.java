package faang.school.analytics.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public enum IntervalType {
    HOUR("HOUR", LocalDateTime.now().minusHours(1)),
    DAY("DAY", LocalDate.now().atStartOfDay()),
    WEEK("WEEK", LocalDate.now().with(java.time.DayOfWeek.MONDAY).atStartOfDay()),
    MONTH("MONTH", LocalDate.now().withDayOfMonth(1).atStartOfDay()),
    YEAR("YEAR", LocalDate.now().withDayOfYear(1).atStartOfDay());

    private final String value;
    private final LocalDateTime start;

    IntervalType(String value, LocalDateTime start) {
        this.value = value;
        this.start = start;
    }
}
