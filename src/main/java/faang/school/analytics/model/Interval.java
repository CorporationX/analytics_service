package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Interval {

    DAY(1),
    WEEK(7),
    MONTH(30),
    YEAR(365);

    private final int days;

    public static Interval getInterval(String interval) {

        if (interval.isBlank()) {
            return null;
        }

        if (NumberUtils.isDigits(interval)) {
            return getIntervalByDays(Integer.parseInt(interval));
        }

        return Interval.valueOf(interval.toUpperCase());
    }

    public static Interval getIntervalByDays(int numberOfInterval) {
        return Arrays.stream(Interval.values())
                .filter(intervalEnum -> intervalEnum.getDays() == numberOfInterval)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown interval: " + numberOfInterval));

    }
}
