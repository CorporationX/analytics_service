package faang.school.analytics.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public enum Interval {
    LAST_15_MINUTES {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusMinutes(15);
        }
    },
    LAST_30_MINUTES {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusMinutes(30);
        }
    },
    LAST_1_HOUR {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusHours(1);
        }
    },
    LAST_3_HOURS {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusHours(3);
        }
    },
    LAST_6_HOURS {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusHours(6);
        }
    },
    LAST_12_HOURS {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusHours(12);
        }
    },
    LAST_24_HOURS {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusHours(24);
        }
    },
    LAST_2_DAYS {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusDays(2);
        }
    },
    LAST_7_DAYS {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusDays(7);
        }
    },
    LAST_30_DAYS {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusDays(30);
        }
    },
    LAST_90_DAYS {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusDays(90);
        }
    },
    LAST_6_MONTHS {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusMonths(6);
        }
    },
    LAST_1_YEAR {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusYears(1);
        }
    },
    TODAY {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        }
    },
    YESTERDAY {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS);
        }
    },
    THIS_WEEK {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().with(java.time.DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
        }
    },
    PREVIOUS_WEEK {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusWeeks(1).with(java.time.DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
        }
    },
    THIS_MONTH {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        }
    },
    PREVIOUS_MONTH {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusMonths(1).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        }
    },
    THIS_YEAR {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().withDayOfYear(1).truncatedTo(ChronoUnit.DAYS);
        }
    },
    PREVIOUS_YEAR {
        @Override
        public LocalDateTime getStartTime() {
            return LocalDateTime.now().minusYears(1).withDayOfYear(1).truncatedTo(ChronoUnit.DAYS);
        }
    };

    public abstract LocalDateTime getStartTime();

    public LocalDateTime getEndTime() {
        return LocalDateTime.now();
    }

    public static Interval of(String interval) {
        if (interval != null) {
            for (Interval intervalType : Interval.values()) {
                if (intervalType.name().equalsIgnoreCase(interval)) {
                    return intervalType;
                }
            }

            throw new IllegalArgumentException("Unknown interval type: " + interval);
        } else {
            return null;
        }
    }
}
