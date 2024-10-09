package faang.school.analytics.model;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public enum TimeUnit {
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public static LocalDateTime startDate(int quantity, TimeUnit timeUnit) {
        try {
            return switch (timeUnit) {
                case HOUR  -> LocalDateTime.now().minusHours(quantity);
                case DAY -> LocalDateTime.now().minusDays(quantity);
                case WEEK -> LocalDateTime.now().minusWeeks(quantity);
                case MONTH -> LocalDateTime.now().minusMonths(quantity);
                case YEAR -> LocalDateTime.now().minusYears(quantity);
            };
        } catch (IllegalArgumentException e) {
            log.warn("No interval value was found for {}", timeUnit);
        }
        return null;
    }
}
