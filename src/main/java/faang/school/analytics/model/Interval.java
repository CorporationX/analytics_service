package faang.school.analytics.model;

import java.time.LocalDateTime;
import java.util.function.Function;

public enum Interval {
    DAY(dateTime -> dateTime.minusDays(1)),
    WEEK(dateTime -> dateTime.minusWeeks(1)),
    MONTH(dateTime -> dateTime.minusMonths(1)),
    YEAR(dateTime -> dateTime.minusYears(1));

    private final Function<LocalDateTime, LocalDateTime> adjuster;

    Interval(Function<LocalDateTime, LocalDateTime> adjuster) {
        this.adjuster = adjuster;
    }

    public LocalDateTime apply(LocalDateTime dateTime) {
        return adjuster.apply(dateTime);
    }
}
