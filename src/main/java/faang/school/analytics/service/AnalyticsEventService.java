package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeInterval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private Map<TimeInterval, Supplier<LocalDateTime>> intervalConverter = new HashMap<>();

    @PostConstruct
    public void init() {
        intervalConverter.put(TimeInterval.SECOND, () -> LocalDateTime.now().minusSeconds(1));
        intervalConverter.put(TimeInterval.MINUTE, () -> LocalDateTime.now().minusMinutes(1));
        intervalConverter.put(TimeInterval.HOUR, () -> LocalDateTime.now().minusHours(1));
        intervalConverter.put(TimeInterval.DAY, () -> LocalDateTime.now().minusDays(1));
        intervalConverter.put(TimeInterval.WEEK, () -> LocalDateTime.now().minusWeeks(1));
        intervalConverter.put(TimeInterval.MOUNT, () -> LocalDateTime.now().minusMonths(1));
        intervalConverter.put(TimeInterval.YEAR, () -> LocalDateTime.now().minusYears(1));
    }

    @Transactional
    public AnalyticsEvent saveEvent(AnalyticsEvent event) {
        return analyticsEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getAnalytics(long receiverId, EventType eventType) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getAnalyticsByInterval(long receiverId, EventType eventType, TimeInterval interval) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = intervalConverter.get(interval).get();
        return getAnalyticsBetweenDates(receiverId, eventType, start, end);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getAnalyticsBetweenDates(long receiverId,
                                                         EventType eventType,
                                                         LocalDateTime start,
                                                         LocalDateTime end) {
        return analyticsEventRepository.findEventsByTime(receiverId, eventType, start, end);
    }
}
