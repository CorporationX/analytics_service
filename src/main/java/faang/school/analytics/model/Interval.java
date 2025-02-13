package faang.school.analytics.model;

import java.time.LocalDateTime;

public enum Interval {
    DAY(1),
    WEEK(7),
    MONTH(30);

    private final int days;

    Interval(int days) {
        this.days = days;
    }

    public boolean contains(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        return !date.isBefore(now.minusDays(days)) && !date.isAfter(now);
    }

    public LocalDateTime getStartTime() {
        return LocalDateTime.now().minusDays(days);
    }

    public int getDays() {
        return days;
    }
}
