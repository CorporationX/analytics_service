package faang.school.analytics.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public enum Interval {
    TODAY,
    YESTERDAY,
    WEEK,
    MONTH,
    QUARTER,
    HALF_YEAR,
    YEAR,
    UNKNOWN;

    public static Interval of(int type) {
        for (Interval interval : Interval.values()) {
            if (interval.ordinal() == type) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown interval: " + type);
    }

    public static DateRange getDateRange(Interval interval) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        LocalDateTime now = now();

        switch (interval) {
            case TODAY:
                startDate = now().withHour(0).withMinute(0).withSecond(0);
                endDate = now().withHour(23).withMinute(59).withSecond(59);
                break;
            case YESTERDAY:
                startDate = now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
                endDate = now().minusDays(1).withHour(23).withMinute(59).withSecond(59);
                break;
            case WEEK:
                startDate = now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
                endDate = now.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
                break;
            case MONTH:
                startDate = now().withHour(0).withMinute(0).withSecond(0).withDayOfMonth(1);
                endDate = now().withHour(23).withMinute(59).withSecond(59);
                break;
            case QUARTER:
                int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                startDate = now.withMonth((currentQuarter - 1) * 3 + 1).withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
                endDate = startDate.plusMonths(3).minusNanos(1);

                break;
            case HALF_YEAR:
                int currentHalfYear = (now.getMonthValue() <= 6) ? 1 : 2;
                startDate = now.withMonth((currentHalfYear - 1) * 6 + 1).withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);

                endDate = startDate.plusMonths(6).minusNanos(1);
                break;
            case YEAR:
                startDate = now().withHour(0).withMinute(0).withSecond(0).withDayOfYear(1);
                endDate = now().withHour(23).withMinute(59).withSecond(59).withDayOfYear(365);
                break;
        }
        return new DateRange(startDate, endDate);
    }
}
