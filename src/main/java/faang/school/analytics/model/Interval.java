package faang.school.analytics.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public enum Interval {
    DAY(ChronoUnit.DAYS),
    WEEK(ChronoUnit.WEEKS),
    MONTH(ChronoUnit.MONTHS),
    YEAR(ChronoUnit.YEARS);

    private final ChronoUnit unit;

    Interval(ChronoUnit unit) {
        this.unit = unit;
    }

    public LocalDateTime subtractFrom(LocalDateTime dateTime) {
        return dateTime.minus(1, unit);
    }
}
       
