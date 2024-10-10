package faang.school.analytics.model;

public enum Interval {
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public static Interval of(int type) {
        for (Interval interval : Interval.values()) {
            if (interval.ordinal() == type) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown interval: " + type);
    }
}
