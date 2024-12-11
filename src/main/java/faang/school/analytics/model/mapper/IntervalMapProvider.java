package faang.school.analytics.model.mapper;

import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

@Component
public class IntervalMapProvider {
    public Map<Interval, LocalDateTime> getIntervalMap() {
        Map<Interval, LocalDateTime> map = new EnumMap<>(Interval.class);
        map.put(Interval.HOUR, LocalDateTime.now().minusHours(1));
        map.put(Interval.DAY, LocalDateTime.now().minusDays(1));
        map.put(Interval.WEEK, LocalDateTime.now().minusDays(7));
        map.put(Interval.MONTH, LocalDateTime.now().minusMonths(1));
        map.put(Interval.YEAR, LocalDateTime.now().minusYears(1));
        return map;
    }
}
