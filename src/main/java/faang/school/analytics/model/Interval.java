package faang.school.analytics.model;

import java.time.LocalDateTime;

public enum Interval {
    DAY, WEEK, MONTH, YEAR;
    
    public LocalDateTime subtractFrom(LocalDateTime dateTime) {
        return switch (this) {
            case DAY -> dateTime.minusDays(1);
            case WEEK -> dateTime.minusWeeks(1);
            case MONTH -> dateTime.minusMonths(1);
            case YEAR -> dateTime.minusYears(1);
        };

    }

}

       
