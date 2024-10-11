package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeInterval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static faang.school.analytics.model.TimeInterval.DAY;
import static faang.school.analytics.model.TimeInterval.HOUR;
import static faang.school.analytics.model.TimeInterval.MINUTE;
import static faang.school.analytics.model.TimeInterval.MONTH;
import static faang.school.analytics.model.TimeInterval.SECOND;
import static faang.school.analytics.model.TimeInterval.WEEK;
import static faang.school.analytics.model.TimeInterval.YEAR;


@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private Map<TimeInterval, Function<LocalDateTime, LocalDateTime>> intervalConverter = new HashMap<>() {{
        put(SECOND, time -> time.minusSeconds(1));
        put(MINUTE, time -> time.minusMinutes(1));
        put(HOUR, time -> time.minusHours(1));
        put(DAY, time -> time.minusDays(1));
        put(WEEK, time -> time.minusWeeks(1));
        put(MONTH, time -> time.minusMonths(1));
        put(YEAR, time -> time.minusYears(1));
    }};

    @Transactional
    public AnalyticsEvent saveEvent(AnalyticsEvent event) {
        return analyticsEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getAllAnalytics(long receiverId, EventType eventType) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getAnalyticsInInterval(Long receiverId, EventType type, TimeInterval interval) {
        if (Objects.isNull(interval)) {
            throw new IllegalArgumentException("Interval can`t be null");
        }
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = intervalConverter.get(interval).apply(end);
        return analyticsEventRepository.findEventsBetweenTimes(receiverId, type, start, end);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getAnalyticsBetweenTime(Long receiverId, EventType type, LocalDateTime start, LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end) || start.isAfter(end)) {
            throw new IllegalArgumentException("Incorrect start or end time");
        }
        return analyticsEventRepository.findEventsBetweenTimes(receiverId, type, start, end);
    }
}
