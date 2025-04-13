package faang.school.analytics.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

public enum Interval {
    DAY {
        @Override
        public LocalDateTime getStart(LocalDateTime baseTime) {
            return baseTime.toLocalDate().atStartOfDay();
        }

        @Override
        public LocalDateTime getEnd(LocalDateTime baseTime) {
            return baseTime.toLocalDate().plusDays(1).atStartOfDay();
        }
    },
    WEEK {
        private static final WeekFields WEEK_FIELDS = WeekFields.of(Locale.getDefault());

        @Override
        public LocalDateTime getStart(LocalDateTime baseTime) {
            LocalDate baseDate = baseTime.toLocalDate();
            return baseDate
                    .with(TemporalAdjusters.previousOrSame(WEEK_FIELDS.getFirstDayOfWeek()))
                    .atStartOfDay();
        }

        @Override
        public LocalDateTime getEnd(LocalDateTime baseTime) {
            LocalDate baseDate = baseTime.toLocalDate();
            return baseDate
                    .with(TemporalAdjusters.nextOrSame(WEEK_FIELDS.getFirstDayOfWeek()))
                    .atStartOfDay();
        }
    },
    MONTH {
        @Override
        public LocalDateTime getStart(LocalDateTime baseTime) {
            return YearMonth.from(baseTime).atDay(1).atStartOfDay();
        }

        @Override
        public LocalDateTime getEnd(LocalDateTime baseTime) {
            return YearMonth.from(baseTime).plusMonths(1).atDay(1).atStartOfDay();
        }
    },
    YEAR {
        @Override
        public LocalDateTime getStart(LocalDateTime baseTime) {
            return LocalDate.of(baseTime.getYear(), 1, 1).atStartOfDay();
        }

        @Override
        public LocalDateTime getEnd(LocalDateTime baseTime) {
            return LocalDate.of(baseTime.getYear() + 1, 1, 1).atStartOfDay();
        }
    };

    public abstract LocalDateTime getStart(LocalDateTime baseTime);

    public abstract LocalDateTime getEnd(LocalDateTime baseTime);
}
