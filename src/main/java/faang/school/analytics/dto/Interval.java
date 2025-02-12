package faang.school.analytics.dto;

import java.time.LocalDateTime;

public enum Interval {
    TODAY {
        @Override
        public boolean isWithinInterval(LocalDateTime dateTime) {
            LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
            return !dateTime.isBefore(startOfToday);
        }
    },
    LAST_WEEK {
        @Override
        public boolean isWithinInterval(LocalDateTime dateTime) {
            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            return !dateTime.isBefore(oneWeekAgo);
        }
    },
    LAST_MONTH {
        @Override
        public boolean isWithinInterval(LocalDateTime dateTime) {
            LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
            return !dateTime.isBefore(oneMonthAgo);
        }
    };

    public abstract boolean isWithinInterval(LocalDateTime dateTime);
}
