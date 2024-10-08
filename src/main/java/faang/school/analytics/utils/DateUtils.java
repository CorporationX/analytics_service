package faang.school.analytics.utils;

import java.time.LocalDateTime;

public class DateUtils {
    public static boolean isBetweenInclusive(LocalDateTime date, LocalDateTime from, LocalDateTime to) {
        if (date == null || from == null || to == null) {
            throw new IllegalArgumentException("Date, from or to not be null");
        }
        return (date.isAfter(from) || date.isEqual(from)) &&
                (date.isBefore(to) || date.isEqual(to));
    }
}
