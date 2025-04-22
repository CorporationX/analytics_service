package faang.school.analytics.service;

import lombok.Getter;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

@Getter
public enum Interval {

    MINUTE(Duration.ofMinutes(1)),
    HOUR(Duration.ofHours(1)),
    DAY(Period.ofDays(1)),
    WEEK(Period.ofWeeks(1)),
    MONTH(Period.ofMonths(1));

    private final TemporalAmount amount;

    Interval(TemporalAmount amount) {
        this.amount = amount;
    }
}
