package faang.school.analytics.model;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public enum Interval {
    LAST_DAY (LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN), LocalDateTime.now()),
    LAST_WEEK (LocalDateTime.of(LocalDate.now().minusWeeks(1), LocalTime.MIN), LocalDateTime.now()),
    LAST_MONTH (LocalDateTime.of(LocalDate.now().minusMonths(1), LocalTime.MIN), LocalDateTime.now()),
    LAST_THREE_MONTHS (LocalDateTime.of(LocalDate.now().minusMonths(3), LocalTime.MIN), LocalDateTime.now()),
    LAST_SIX_MONTHS (LocalDateTime.of(LocalDate.now().minusMonths(6), LocalTime.MIN), LocalDateTime.now()),
    YEAR_TO_DATE (LocalDateTime.of(LocalDate.now().getYear(),1,1,0,0), LocalDateTime.now());

    private final LocalDateTime from;
    private final LocalDateTime to;

    Interval(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }
}
