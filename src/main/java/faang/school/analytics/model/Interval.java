package faang.school.analytics.model;

public enum Interval {
    DAY,
    WEEK,
    MONTH,
    QUARTER,
    YEAR;

    public long toSeconds() {
        return switch (this) {
            case DAY -> 24 * 60 * 60L;
            case WEEK -> 7 * 24 * 60 * 60L;
            case MONTH -> 30 * 24 * 60 * 60L;
            case QUARTER -> 91 * 24 * 60 * 60L;
            case YEAR -> 365 * 24 * 60 * 60L;
        };
    }
}
