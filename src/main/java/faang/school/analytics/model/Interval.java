package faang.school.analytics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Interval {
    MINUTE(60, "Minute"),
    HOUR(3600, "Hour"),
    DAY(86400, "Day"),
    WEEK(604800, "Week"),
    MONTH(2592000, "Month"),
    YEAR(31536000, "Year");

    private final int seconds;
    private final String description;
}
