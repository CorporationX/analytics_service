package faang.school.analytics.service;

import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class IntervalService {
    public LocalDateTime getFrom(Interval interval) {
        return switch (interval) {
            case LAST_DAY -> calculateFrom(1, ChronoUnit.DAYS);
            case LAST_WEEK -> calculateFrom(1, ChronoUnit.WEEKS);
            case LAST_MONTH -> calculateFrom(1, ChronoUnit.MONTHS);
            case LAST_THREE_MONTHS -> calculateFrom(3, ChronoUnit.MONTHS);
            case LAST_SIX_MONTHS -> calculateFrom(6, ChronoUnit.MONTHS);
            case YEAR_TO_DATE -> LocalDateTime.of(LocalDate.now().getYear(), 1, 1, 0, 0);
            case ALL_TIME -> LocalDateTime.of(1970, 1, 1, 0, 0);
            default -> throw new IllegalArgumentException("Неподдерживаемый интервал: " + interval);
        };
    }

    public LocalDateTime getTo() {
        return LocalDateTime.now();
    }

    private LocalDateTime calculateFrom(long amount, ChronoUnit unit) {
        return LocalDateTime.of(LocalDate.now().minus(amount, unit), LocalTime.MIN);
    }
}
