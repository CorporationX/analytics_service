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
    public List<AnalyticsEvent> getAnalytics(long receiverId,
                                             EventType eventType,
                                             TimeInterval interval,
                                             LocalDateTime start,
                                             LocalDateTime end) {

        if (isNonTimeFilter(interval, start, end)) {
            return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        }

        if (isIntervalFilter(interval)) {
            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = intervalConverter.get(interval).apply(endTime);
            return analyticsEventRepository.findEventsBetweenTimes(receiverId, eventType, startTime, endTime);
        }

        if (isBetweenTimesFilters(start, end)) {
            return analyticsEventRepository.findEventsBetweenTimes(receiverId, eventType, start, end);
        }

        throw new IllegalArgumentException("Chose interval or between times args");
    }

    private boolean isBetweenTimesFilters(LocalDateTime start, LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return false;
        }
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start can`t be after end");
        }
            return true;
    }

    private boolean isIntervalFilter(TimeInterval interval) {
        return Objects.nonNull(interval);
    }

    private boolean isNonTimeFilter(TimeInterval interval, LocalDateTime start, LocalDateTime end) {
        return Objects.isNull(interval) && Objects.isNull(start) && Objects.isNull(end);
    }
}
