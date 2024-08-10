package faang.school.analytics.model;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public enum Interval {


    DAY(1, 0),
    WEEK(7, 0),
    MONTH(30, 0),
    YEAR(365, 0),
    ;

    private final int daysIntervalFrom;
    private final int daysIntervalTo;

    public LocalDateTime getFrom(LocalDateTime localDateTime) {
        return localDateTime.minusDays(daysIntervalFrom);
    }

    public LocalDateTime getTo(LocalDateTime localDateTime) {
        return localDateTime.minusDays(daysIntervalTo);
    }
}
