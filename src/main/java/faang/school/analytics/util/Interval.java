package faang.school.analytics.util;

import java.time.LocalDateTime;
import java.util.Arrays;

import static java.time.LocalDateTime.now;

//@Builder
public enum Interval {
    LAST_DAY(now().minusDays(1), now()),
    LAST_WEEK(now().minusWeeks(1), now()),
    LAST_MONTH(now().minusMonths(1), now()),
    LAST_SIX_MONTH(now().minusMonths(6), now()),
    LAST_YEAR(now().minusYears(1), now());

    LocalDateTime start;
    LocalDateTime end;
     Interval(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.start = start;
        this.end = end;
    }

    public boolean contains(LocalDateTime dateTime) {
        return (dateTime.isEqual(start) || dateTime.isAfter(start)) &&
                (dateTime.isEqual(end) || dateTime.isBefore(end));
    }

    public static Interval fromStringOrNumber(Object value){
        if (value instanceof String strValue) {
            return Arrays.stream(values())
                    .filter(interval -> interval.name().equalsIgnoreCase(strValue))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid interval string: " + strValue));
        } else if (value instanceof Number) {
            int numValue = ((Number) value).intValue();
            return switch (numValue) {
                case 1 -> LAST_DAY;
                case 7 -> LAST_WEEK;
                case 30 -> LAST_MONTH;
                case 180 -> LAST_SIX_MONTH;
                case 366 -> LAST_YEAR;
                default -> throw new IllegalArgumentException("Invalid interval number: " + numValue);
            };
        } else {
            throw new IllegalArgumentException("Invalid interval value type: " + value);
        }
    }
}
