package faang.school.analytics.dto;

import java.time.LocalDateTime;

public enum Interval {
    TODAY {
        @Override
        public LocalDateTime getStart() {
            return LocalDateTime.now().toLocalDate().atStartOfDay();
        }
    },
    LAST_WEEK {
        @Override
        public LocalDateTime getStart() {
            return LocalDateTime.now().minusWeeks(1);
        }
    },
    LAST_MONTH {
        @Override
        public LocalDateTime getStart() {
            return LocalDateTime.now().minusMonths(1);
        }
    };

    public abstract LocalDateTime getStart();
}
