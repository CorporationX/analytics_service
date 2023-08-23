package faang.school.analytics.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public enum Interval {
    TODAY,
    YESTERDAY,
    WEEK,
    HALF_MONTH,
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

    public static Interval parseInterval(String input) {
        try {
            int intervalValue = Integer.parseInt(input);
            return Interval.of(intervalValue);
        } catch (NumberFormatException e) {
            try {
                return Interval.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return Interval.UNKNOWN;
            }
        }
    }

    public static DateRange getDateRange(Interval interval) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        LocalDateTime now = LocalDateTime.now();

        switch (interval) {
            case TODAY:
                startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
                endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
                break;
            case YESTERDAY:
                startDate = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
                endDate = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59);
                break;
            case WEEK:
                LocalDateTime startOfWeek = now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
                LocalDateTime endOfWeek = now.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
                break;
            case HALF_MONTH:
                int dayOfMonth = now.getDayOfMonth();
                LocalDateTime startOfHalfMonth;
                LocalDateTime endOfHalfMonth;

                if (dayOfMonth <= 15) {
                    startOfHalfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                    endOfHalfMonth = now.withDayOfMonth(15).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
                } else {
                    startOfHalfMonth = now.withDayOfMonth(16).withHour(0).withMinute(0).withSecond(0).withNano(0);
                    endOfHalfMonth = now.withDayOfMonth(now.getMonth().length(now.toLocalDate().isLeapYear()) / 2).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
                }
                break;
            case MONTH:
                startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withDayOfMonth(1);
                endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
                break;
            case QUARTER:
                int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                LocalDateTime startOfQuarter = now.withMonth((currentQuarter - 1) * 3 + 1).withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
                LocalDateTime endOfQuarter = startOfQuarter.plusMonths(3).minusNanos(1);

                break;
            case HALF_YEAR:
                int currentHalfYear = (now.getMonthValue() <= 6) ? 1 : 2;
                LocalDateTime startOfHalfYear = now.withMonth((currentHalfYear - 1) * 6 + 1).withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);

                LocalDateTime endOfHalfYear = startOfHalfYear.plusMonths(6).minusNanos(1);
                break;
            case YEAR:
                startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withDayOfYear(1);
                endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withDayOfYear(365);
                break;
        }
        return new DateRange(startDate, endDate);
    }
}
