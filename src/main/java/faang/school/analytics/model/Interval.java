package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Period;

@AllArgsConstructor
@Getter
public enum Interval {
    DAY(Period.ofDays(1)),
    WEEK(Period.ofWeeks(1)),
    MONTH(Period.ofMonths(1)),
    QUARTER(Period.ofMonths(3)),
    HALF_YEAR(Period.ofMonths(6)),
    THREE_QUARTER(Period.ofMonths(9)),
    YEAR(Period.ofYears(1));

    private final Period period;
}
