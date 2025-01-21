package faang.school.analytics.dto;


public enum IntervalDto {
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public static IntervalDto of(String interval) {
        return IntervalDto.valueOf(interval.toUpperCase());
    }
}
