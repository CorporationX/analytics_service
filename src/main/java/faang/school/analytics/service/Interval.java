package faang.school.analytics.service;

import lombok.Getter;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Getter
public enum Interval {
    DAY(1),
    WEEK(7),
    MONTH(30),
    YEAR(365);

    private final Integer days;
    private final TemporalUnit unit = ChronoUnit.DAYS;

    Interval(int days) {
        this.days = days;
    }
}
