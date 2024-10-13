package faang.school.analytics.service;

import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Service
public class TimeIntervalsService {

    public static final String START_INTERVAL = "start interval";
    public static final String END_INTERVAL = "end interval";

    public Map<String, LocalDateTime> getInterval(String interval) {
        Map<String, LocalDateTime> mapInterval = new HashMap<>();
        Interval strToInterval = Interval.valueOf(interval);
        LocalDateTime now = LocalDateTime.now();

        if (strToInterval.equals(Interval.DAY)) {
            LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

            mapInterval.put(START_INTERVAL, startOfDay);
            mapInterval.put(END_INTERVAL, endOfDay);
        }

        if (strToInterval.equals(Interval.WEEK)) {
            LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
            LocalDateTime endOfWeek = startOfWeek.plusWeeks(1).minusSeconds(1);

            mapInterval.put(START_INTERVAL, startOfWeek);
            mapInterval.put(END_INTERVAL, endOfWeek);
        }

        if (strToInterval.equals(Interval.MONTH)) {
            LocalDateTime startOfMonth = now.withDayOfMonth(1);
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).toLocalDate().atStartOfDay().minusSeconds(1);

            mapInterval.put(START_INTERVAL, startOfMonth);
            mapInterval.put(END_INTERVAL, endOfMonth);
        }

        if (strToInterval.equals(Interval.YEAR)) {
            LocalDateTime startOfYear = now.withDayOfYear(1);
            LocalDateTime endOfYear = startOfYear.plusYears(1).toLocalDate().atStartOfDay().minusSeconds(1);

            mapInterval.put(START_INTERVAL, startOfYear);
            mapInterval.put(END_INTERVAL, endOfYear);
        }

        return mapInterval;
    }
}
