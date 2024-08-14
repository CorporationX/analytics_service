package faang.school.analytics.model;

import lombok.Getter;

@Getter
public enum Interval {
    DAY(2),
    WEEK(8),
    MONTH(31),
    YEAR(365),
    ALL_TIME(0);

    private final int value;

    Interval(int value){
        this.value = value;
    }

}
