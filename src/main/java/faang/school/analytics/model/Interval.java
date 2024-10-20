package faang.school.analytics.model;

import java.time.LocalDateTime;

public enum Interval {
    JANUARY_FEBRUARY(LocalDateTime.of(2024, 1, 1, 0, 0, 0), LocalDateTime.of(2024, 2, 29, 23, 59, 59)),
    FEBRUARY_MARCH(LocalDateTime.of(2024, 2, 1, 0, 0, 0), LocalDateTime.of(2024, 3, 31, 23, 59, 59)),
    MARCH_APRIL(LocalDateTime.of(2024, 3, 1, 0, 0, 0), LocalDateTime.of(2024, 4, 30, 23, 59, 59)),
    APRIL_MAY(LocalDateTime.of(2024, 4, 1, 0, 0, 0), LocalDateTime.of(2024, 5, 31, 23, 59, 59)),
    MAY_JUNE(LocalDateTime.of(2024, 5, 1, 0, 0, 0), LocalDateTime.of(2024, 6, 30, 23, 59, 59)),
    JUNE_JULY(LocalDateTime.of(2024, 6, 1, 0, 0, 0), LocalDateTime.of(2024, 7, 31, 23, 59, 59)),
    JULY_AUGUST(LocalDateTime.of(2024, 7, 1, 0, 0, 0), LocalDateTime.of(2024, 8, 31, 23, 59, 59)),
    AUGUST_SEPTEMBER(LocalDateTime.of(2024, 8, 1, 0, 0, 0), LocalDateTime.of(2024, 9, 30, 23, 59, 59)),
    SEPTEMBER_OCTOBER(LocalDateTime.of(2024, 9, 1, 0, 0, 0), LocalDateTime.of(2024, 10, 31, 23, 59, 59)),
    OCTOBER_NOVEMBER(LocalDateTime.of(2024, 10, 1, 0, 0, 0), LocalDateTime.of(2024, 11, 30, 23, 59, 59)),
    NOVEMBER_DECEMBER(LocalDateTime.of(2024, 11, 1, 0, 0, 0), LocalDateTime.of(2024, 12, 31, 23, 59, 59)),
    DECEMBER_JANUARY(LocalDateTime.of(2024, 12, 1, 0, 0, 0), LocalDateTime.of(2025, 1, 31, 23, 59, 59));

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    Interval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public static Interval of(int type) {
        for (Interval interval : Interval.values()) {
            if (interval.ordinal() == type) {
                return interval;
            }
        }
       return null;
    }
}
