package faang.school.analytics.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Interval {
    DAY_1(1),
    DAY_3(3),
    DAY_14(14),
    DAY_30(30),
    DAY_90(90),
    MONTH_1(30),
    MONTH_3(90),
    MONTH_6(180),
    MONTH_12(365),
    YEAR_1(365);

    private final int days;

    public static Interval of(int intervalOrder) {
        for (Interval interval : Interval.values()) {
            if (interval.ordinal() == intervalOrder) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown interval: " + intervalOrder);
    }
}
