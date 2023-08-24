package faang.school.analytics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Interval {
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    YEAR("year");

    private final String period;

    public static Interval get(String periodName){
        for(Interval interval : values()){
            if(interval.getPeriod().equals(periodName)){
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown period type: " + periodName);
    }
}
