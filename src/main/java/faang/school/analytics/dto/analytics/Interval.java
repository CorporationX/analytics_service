package faang.school.analytics.dto.analytics;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public enum Interval {

    DAY(LocalDateTime.now().minusDays(1), LocalDateTime.now()),
    MONTH(LocalDateTime.now().minusMonths(1), LocalDateTime.now()),
    YEAR(LocalDateTime.now().minusYears(1), LocalDateTime.now());

    private final LocalDateTime from;
    private final LocalDateTime to;

    Interval(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public static Interval of(String range) {
        if (isInteger(range)) {
            for (Interval interval : Interval.values()) {
                if (interval.ordinal() == Integer.parseInt(range)) {
                    return interval;
                }
            }
        } else {
            for (Interval interval : Interval.values()) {
                if (interval.name().equalsIgnoreCase(range)) {
                    return interval;
                }
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + range);
    }

    private static boolean isInteger(String type) {
        return type.matches("\\d+");
    }
}
