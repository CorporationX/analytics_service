package faang.school.analytics.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public enum Intervals {
    MINUTES(ChronoUnit.MINUTES),
    HOURS(ChronoUnit.HOURS),
    DAYS(ChronoUnit.DAYS),
    WEEKS(ChronoUnit.WEEKS),
    MONTHS(ChronoUnit.MONTHS);

    private final ChronoUnit chronoUnit;

    Intervals(ChronoUnit chronoUnit) {
        this.chronoUnit = chronoUnit;
    }

    public LocalDateTime dateFrom(LocalDateTime now, int amount) {
        return now.minus(amount, chronoUnit);
    }
}
