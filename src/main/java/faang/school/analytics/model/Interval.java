package faang.school.analytics.model;

import lombok.Getter;

import java.time.Period;
import java.time.temporal.TemporalAmount;

@Getter
public enum Interval {
    DAY(Period.ofDays(1)),
    WEEK(Period.ofWeeks(1)),
    MONTH(Period.ofMonths(1)),
    YEAR(Period.ofYears(1));


    private final TemporalAmount timeInterval;

    Interval(Period timeInterval) {
        this.timeInterval = timeInterval;
    }
}
