package faang.school.analytics.model;

import faang.school.analytics.exception.IllegalModelException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Interval {
    DAY(1),
    WEEK(7),
    MONTH(30),
    QUARTER(90),
    YEAR(365);

    private final int days;

    public static Interval getInterval(int days) {
        return Arrays.stream(Interval.values())
            .filter(interval -> interval.getDays() == days)
            .findFirst()
            .orElseThrow(() -> new IllegalModelException("Invalid interval for: " + days));
    }
}
