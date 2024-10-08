package faang.school.analytics.model;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public enum Interval {
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public static LocalDateTime startDate(String input) {
        String[] parts = input.split(" ");
        int quantity = Integer.parseInt(parts[0]);
        String unit = parts[1].toUpperCase();

        try {
            if(unit.endsWith("S")){
                unit = unit.replace("S", "").trim();
            }
            Interval interval = Interval.valueOf(unit);
            return switch (interval) {
                case HOUR  -> LocalDateTime.now().minusHours(quantity);
                case DAY -> LocalDateTime.now().minusDays(quantity);
                case WEEK -> LocalDateTime.now().minusWeeks(quantity);
                case MONTH -> LocalDateTime.now().minusMonths(quantity);
                case YEAR -> LocalDateTime.now().minusYears(quantity);
            };
        } catch (IllegalArgumentException e) {
            log.warn("No interval value was found for unit string: {}", unit);
        }
        return null;
    }
}
