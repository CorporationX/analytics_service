package faang.school.analytics.model;

import lombok.Getter;

@Getter
public enum Interval {
    LAST_DAY,
    LAST_WEEK,
    LAST_MONTH,
    LAST_THREE_MONTHS,
    LAST_SIX_MONTHS,
    YEAR_TO_DATE,
    ALL_TIME
}