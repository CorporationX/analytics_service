package faang.school.analytics.model;

import java.time.LocalDateTime;

/**
 * @author Evgenii Malkov
 */
public enum Interval {

    LAST_DAY {
        @Override
        public boolean includes(LocalDateTime dateTime) {
            LocalDateTime now = LocalDateTime.now();
            return !dateTime.isBefore(now.minusDays(1));
        }
    },
    LAST_WEEK {
        @Override
        public boolean includes(LocalDateTime dateTime) {
            LocalDateTime now = LocalDateTime.now();
            return !dateTime.isBefore(now.minusWeeks(1));
        }
    },
    LAST_MONTH {
        @Override
        public boolean includes(LocalDateTime dateTime) {
            LocalDateTime now = LocalDateTime.now();
            return !dateTime.isBefore(now.minusMonths(1));
        }
    },
    LAST_QUARTER {
        @Override
        public boolean includes(LocalDateTime dateTime) {
            LocalDateTime now = LocalDateTime.now();
            return !dateTime.isBefore(now.minusMonths(3));
        }
    },
    LAST_YEAR {
        @Override
        public boolean includes(LocalDateTime dateTime) {
            LocalDateTime now = LocalDateTime.now();
            return !dateTime.isBefore(now.minusYears(1));
        }
    };

    public abstract boolean includes(LocalDateTime dateTime);
}
