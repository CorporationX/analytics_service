package faang.school.analytics.model;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum Interval {

    DAY(1),
    WEEK(7),
    MONTH(30),
    YEAR(365);

    private final int days;

    public int getDaysByInterval(Interval interval) {

        Interval intervalExist = Arrays.stream(Interval.values())
                .filter(position -> position.equals(interval))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown interval: " + interval));

        return intervalExist.days;
    }
}
