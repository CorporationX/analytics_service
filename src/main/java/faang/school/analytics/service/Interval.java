package faang.school.analytics.service;

import faang.school.analytics.model.EventType;

public enum Interval {
    LAST_DAY,
    LAST_WEEK,
    LAST_MONTH,
    LAST_YEAR;

    public static Interval of(int type) {
        for (Interval interval : Interval.values()) {
            if (interval.ordinal() == type) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown interval: " + type);
    }
}
