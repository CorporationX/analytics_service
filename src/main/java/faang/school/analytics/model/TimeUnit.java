package faang.school.analytics.model;

import faang.school.analytics.exception.IntervalsNotValidException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public enum TimeUnit {
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public static LocalDateTime getStartDate(int quantity, TimeUnit timeUnit) {
        try {
            return switch (timeUnit) {
                case HOUR -> LocalDateTime.now().minusHours(quantity);
                case DAY -> LocalDateTime.now().minusDays(quantity);
                case WEEK -> LocalDateTime.now().minusWeeks(quantity);
                case MONTH -> LocalDateTime.now().minusMonths(quantity);
                case YEAR -> LocalDateTime.now().minusYears(quantity);
            };
        } catch (NullPointerException e) {
            log.error("Neither quantity {} nor timeUnit {} are null: {}", quantity, timeUnit, e.getMessage());
            throw new IntervalsNotValidException("Quantity, TimeUnit or both values are null!");
        }
    }
}
