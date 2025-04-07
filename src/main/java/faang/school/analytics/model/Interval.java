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
        public LocalDateTime getStart() {
            return LocalDate.now().atStartOfDay();
        }

        @Override
        public LocalDateTime getEnd() {
            return LocalDate.now().plusDays(1).atStartOfDay();
        }
    },
    WEEK {
        @Override
        public LocalDateTime getStart() {
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            LocalDate startOfWeek = LocalDate.now()
                    .with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));
            return startOfWeek.atStartOfDay();
        }

        @Override
        public LocalDateTime getEnd() {
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            LocalDate endOfWeek = LocalDate.now()
                    .with(TemporalAdjusters.nextOrSame(weekFields.getFirstDayOfWeek())).plusWeeks(1);
            return endOfWeek.atStartOfDay();
        }
    },
    MONTH {
        @Override
        public LocalDateTime getStart() {
            return YearMonth.now().atDay(1).atStartOfDay();
        }

        @Override
        public LocalDateTime getEnd() {
            return YearMonth.now().plusMonths(1).atDay(1).atStartOfDay();
        }
    },
    YEAR {
        @Override
        public LocalDateTime getStart() {
            return LocalDate.of(LocalDate.now().getYear(), 1, 1).atStartOfDay();
        }

        @Override
        public LocalDateTime getEnd() {
            return LocalDate.of(LocalDate.now().getYear() + 1, 1, 1).atStartOfDay();
        }
    };

    public abstract LocalDateTime getStart();
    public abstract LocalDateTime getEnd();
}
