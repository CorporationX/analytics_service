package faang.school.analytics.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

public enum Interval {
    ONE_MONTH(1),
    THREE_MONTH(3),
    SIX_MONTH(6),
    ONE_YEAR(12);
    private final long offsetInMonths;

    private Interval(long offsetInMonths) {
        this.offsetInMonths = offsetInMonths;
    }

    public LocalDateTime getStartDate(LocalDateTime endDate) {
        LocalDateTime startMonth = endDate.minusMonths(offsetInMonths - 1);
        return startMonth.withDayOfMonth(1);
    }
}
