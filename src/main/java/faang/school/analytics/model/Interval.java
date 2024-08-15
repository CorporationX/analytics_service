package faang.school.analytics.model;

public enum Interval {
    HOURLY,
    DAILY,
    WEEKLY,
    MONTHLY,
    OTHER;

    public static Interval fromString(String value) {
        return Interval.valueOf(value.toUpperCase());
    }
}
